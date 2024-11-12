package main.pedidos;

import java.util.List;
import java.util.concurrent.*;

public class SistemaDePedidos {
    private ExecutorService executorPago;
    private ExecutorService executorEmpaquetado;
    private ExecutorService executorEnvio;
    private PriorityBlockingQueue<Pedido> colaPedidos;

    public ExecutorService getExecutorPago() {
        return executorPago;
    }

    public ExecutorService getExecutorEmpaquetado() {
        return executorEmpaquetado;
    }

    public ExecutorService getExecutorEnvio() {
        return executorEnvio;
    }

    public SistemaDePedidos(int hilosPago, int hilosEmpaquetado, int hilosEnvio) {
        this.executorPago = Executors.newFixedThreadPool(hilosPago);
        this.executorEmpaquetado = Executors.newFixedThreadPool(hilosEmpaquetado);
        this.executorEnvio = Executors.newFixedThreadPool(hilosEnvio);
        this.colaPedidos = new PriorityBlockingQueue<>();
    }

    

    public void agregarPedido(Pedido pedido) {
        colaPedidos.offer(pedido);
    }

    public void procesarPedidos() {
        while (!colaPedidos.isEmpty()) {
            Pedido pedido = colaPedidos.poll();

            CountDownLatch pagoCompleto = new CountDownLatch(1);
            CountDownLatch empaquetadoCompleto = new CountDownLatch(1);

            // Procesar pago
            executorPago.submit(() -> {
                try {
                    System.out.println("Procesando pago para el pedido " + pedido.getIdPedido());
                    Thread.sleep(2000);  // Simula el tiempo de procesamiento
                    pedido.setEstado(Pedido.EstadoPedido.PAGO_REALIZADO);
                    System.out.println("Pago realizado para el pedido " + pedido.getIdPedido());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    pagoCompleto.countDown();  // Señala que el pago se ha completado
                }
            });

            // Empaquetado
            executorEmpaquetado.submit(() -> {
                try {
                    pagoCompleto.await();  // Esperar a que el pago se complete
                    if (pedido.getEstado() == Pedido.EstadoPedido.PAGO_REALIZADO) {
                        System.out.println("Empaquetando pedido " + pedido.getIdPedido());
                        Thread.sleep(3000);  // Simula el tiempo de empaquetado
                        pedido.setEstado(Pedido.EstadoPedido.EMPAQUETADO);
                        System.out.println("main.pedidos.Pedido " + pedido.getIdPedido() + " empaquetado");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    empaquetadoCompleto.countDown();  // Señala que el empaquetado se ha completado
                }
            });

            // Envío
            executorEnvio.submit(() -> {
                try {
                    empaquetadoCompleto.await();  // Esperar a que el empaquetado se complete
                    if (pedido.getEstado() == Pedido.EstadoPedido.EMPAQUETADO) {
                        System.out.println("Enviando pedido " + pedido.getIdPedido());
                        Thread.sleep(2000);  // Simula el tiempo de envío
                        pedido.setEstado(Pedido.EstadoPedido.ENVIADO);
                        System.out.println("main.pedidos.Pedido " + pedido.getIdPedido() + " enviado");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void cerrarSistema() {
        try {
            System.out.println("Esperando a que se completen todos los pedidos...");
            executorPago.shutdown();
            executorEmpaquetado.shutdown();
            executorEnvio.shutdown();

            if (!executorPago.awaitTermination(60, TimeUnit.SECONDS)) executorPago.shutdownNow();
            if (!executorEmpaquetado.awaitTermination(60, TimeUnit.SECONDS)) executorEmpaquetado.shutdownNow();
            if (!executorEnvio.awaitTermination(60, TimeUnit.SECONDS)) executorEnvio.shutdownNow();

            System.out.println("Cerrando el sistema de pedidos...");

        } catch (InterruptedException e) {
            executorPago.shutdownNow();
            executorEmpaquetado.shutdownNow();
            executorEnvio.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        // Aumentar el número de hilos para soportar 100 pedidos
        SistemaDePedidos sistema = new SistemaDePedidos(10, 15, 10);

        // Simular 100 pedidos
        for (int i = 1; i <= 100; i++) {
            boolean esUrgente = i % 10 == 0;  // Cada 10 pedidos será urgente
            Pedido pedido = new Pedido(i, "Cliente " + i, List.of("Producto 1", "Producto 2"), esUrgente);
            sistema.agregarPedido(pedido);
        }

        // Medir el tiempo de procesamiento total
        long inicio = System.currentTimeMillis();

        // Procesar los pedidos
        sistema.procesarPedidos();

        // Cerrar el sistema
        sistema.cerrarSistema();

        long fin = System.currentTimeMillis();
        System.out.println("Tiempo total de procesamiento: " + (fin - inicio) + " ms");
    }
}