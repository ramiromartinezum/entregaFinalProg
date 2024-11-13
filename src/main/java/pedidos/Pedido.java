package main.java.pedidos;

import java.util.List;

public class Pedido implements Comparable<Pedido> {
    private int idPedido;
    private String cliente;
    private List<String> productos;
    private boolean urgente;
    private EstadoPedido estado;
    public enum EstadoPedido {
        PENDIENTE, PAGO_REALIZADO, EMPAQUETADO, ENVIADO
    }
    public Pedido(int idPedido, String cliente, List<String> productos, boolean urgente) {
        this.idPedido = idPedido;
        this.cliente = cliente;
        this.productos = productos;
        this.urgente = urgente;
        this.estado = EstadoPedido.PENDIENTE;
    }

    // Implementación de la comparación: los pedidos urgentes tienen mayor prioridad
    @Override
    public int compareTo(Pedido otroPedido) {
        return Boolean.compare(otroPedido.urgente, this.urgente); // Los urgentes primero
    }

    // Getters y setters sincronizados para acceso seguro
    public int getIdPedido() {
        return idPedido;
    }

    public String getCliente() {
        return cliente;
    }

    public List<String> getProductos() {
        return productos;
    }

    public boolean esUrgente() {
        return urgente;
    }

    // Sincronizar el acceso al estado
    public synchronized EstadoPedido getEstado() {
        return estado;
    }

    // Sincronizar la modificación del estado
    public synchronized void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "main.java.pedidos.Pedido{" +
                "idPedido=" + idPedido +
                ", cliente='" + cliente + '\'' +
                ", productos=" + productos +
                ", urgente=" + urgente +
                ", estado=" + estado +
                '}';
    }
}