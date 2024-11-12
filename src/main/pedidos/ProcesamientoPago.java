package main.pedidos;

import main.pedidos.Pedido;

public class ProcesamientoPago implements Runnable {
    private Pedido pedido;
    public ProcesamientoPago(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void run() {
        System.out.println("Procesando pago para el pedido " + pedido.getIdPedido());
        try {
            Thread.sleep(2000);  // Simulamos el tiempo de procesamiento
            pedido.setEstado(Pedido.EstadoPedido.PAGO_REALIZADO);
            System.out.println("Pago realizado para el pedido " + pedido.getIdPedido());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
