package test.java.pedidos;

import main.java.pedidos.Pedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
public class PedidoTest {
    private Pedido pedidoNormal;
    private Pedido pedidoUrgente;

    @BeforeEach
    public void setUp() {
        // Creamos un pedido normal y un pedido urgente
        pedidoNormal = new Pedido(1, "Cliente A", List.of("Producto 1"), false);
        pedidoUrgente = new Pedido(2, "Cliente B", List.of("Producto 2"), true);
    }

    @Test
    public void testCreacionPedido() {
        // Verificamos que el pedido se haya creado correctamente
        assertEquals(1, pedidoNormal.getIdPedido());
        assertEquals("Cliente A", pedidoNormal.getCliente());
        assertEquals(List.of("Producto 1"), pedidoNormal.getProductos());
        assertFalse(pedidoNormal.esUrgente());

        // Verificamos el pedido urgente
        assertEquals(2, pedidoUrgente.getIdPedido());
        assertTrue(pedidoUrgente.esUrgente());
    }

    @Test
    public void testEstadoInicialPedido() {
        // Verificamos que el estado inicial del pedido sea PENDIENTE
        assertEquals(Pedido.EstadoPedido.PENDIENTE, pedidoNormal.getEstado());
    }

    @Test
    public void testCambioDeEstado() {
        // Cambiamos el estado del pedido a PAGO_REALIZADO y verificamos
        pedidoNormal.setEstado(Pedido.EstadoPedido.PAGO_REALIZADO);
        assertEquals(Pedido.EstadoPedido.PAGO_REALIZADO, pedidoNormal.getEstado());
    }

    @Test
    public void testPrioridadPedidoUrgente() {
        // Verificamos que los pedidos urgentes tengan mayor prioridad
        assertTrue(pedidoUrgente.compareTo(pedidoNormal) < 0, "El pedido urgente debe tener mayor prioridad");
    }
}
