package main.java.pedidos;

import java.util.List;

public class EmpaquetadoPedido implements Runnable {
    private Pedido pedido;

    public EmpaquetadoPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void run() {
        try {
            if (pedido.getEstado() == Pedido.EstadoPedido.PAGO_REALIZADO) {
                System.out.println("Empaquetando pedido " + pedido.getIdPedido());

                // Simular el empaquetado de productos usando streams paralelos
                List<String> productos = pedido.getProductos();
                productos.parallelStream().forEach(producto -> {
                    try {
                        System.out.println("Empaquetando producto: " + producto + " del pedido " + pedido.getIdPedido());
                        Thread.sleep(1000);  // Simula el tiempo de empaquetado de cada producto
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                pedido.setEstado(Pedido.EstadoPedido.EMPAQUETADO);
                System.out.println("main.java.pedidos.Pedido " + pedido.getIdPedido() + " empaquetado");
            } else {
                System.out.println("No se puede empaquetar el pedido " + pedido.getIdPedido() + ". El pago no se ha realizado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
