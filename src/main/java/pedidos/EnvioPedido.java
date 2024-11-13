package main.java.pedidos;

public class EnvioPedido implements Runnable {
    private Pedido pedido;
    public EnvioPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public void run() {
        if (pedido.getEstado() == Pedido.EstadoPedido.EMPAQUETADO) {
            System.out.println("Enviando pedido " + pedido.getIdPedido());
            try {
                Thread.sleep(2000);  // Simulación del proceso de envío
                pedido.setEstado(Pedido.EstadoPedido.ENVIADO);
                System.out.println("main.java.pedidos.Pedido " + pedido.getIdPedido() + " enviado");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se puede enviar el pedido " + pedido.getIdPedido() + ". El pedido no ha sido empaquetado.");
        }
    }
}
