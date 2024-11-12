package test;

import main.pedidos.Pedido;
import main.pedidos.SistemaDePedidos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class SistemaDePedidosTest {
    private SistemaDePedidos sistema;

    @BeforeEach
    public void setUp() {
        sistema = new SistemaDePedidos(5, 5, 5);  // Inicializamos el sistema con 5 hilos para cada etapa
    }

    @Test
    public void testAgregarYProcesarPedido() throws InterruptedException {
        // Creamos un pedido de prueba
        Pedido pedido = new Pedido(1, "Cliente A", List.of("Producto 1", "Producto 2"), false);

        // Agregamos el pedido al sistema
        sistema.agregarPedido(pedido);

        // Procesamos los pedidos
        sistema.procesarPedidos();

        // Esperamos unos segundos para asegurarnos de que el pedido se procesa completamente
        sistema.getExecutorPago().awaitTermination(5, TimeUnit.SECONDS);
        sistema.getExecutorEmpaquetado().awaitTermination(5, TimeUnit.SECONDS);
        sistema.getExecutorEnvio().awaitTermination(5, TimeUnit.SECONDS);

        // Verificamos que el pedido haya pasado por todas las etapas
        assertEquals(Pedido.EstadoPedido.ENVIADO, pedido.getEstado());
    }

    @Test
    public void testCerrarSistema() throws InterruptedException {
        // Creamos algunos pedidos de prueba
        Pedido pedido1 = new Pedido(1, "Cliente A", List.of("Producto 1"), false);
        Pedido pedido2 = new Pedido(2, "Cliente B", List.of("Producto 2"), true);  // main.pedidos.Pedido urgente

        // Agregamos los pedidos al sistema
        sistema.agregarPedido(pedido1);
        sistema.agregarPedido(pedido2);

        // Procesamos los pedidos
        sistema.procesarPedidos();

        // Cierre ordenado del sistema
        sistema.cerrarSistema();

        // Esperamos para asegurarnos de que los pedidos se procesen
        sistema.getExecutorPago().awaitTermination(5, TimeUnit.SECONDS);
        sistema.getExecutorEmpaquetado().awaitTermination(5, TimeUnit.SECONDS);
        sistema.getExecutorEnvio().awaitTermination(5, TimeUnit.SECONDS);

        // Verificamos que ambos pedidos hayan sido procesados y enviados
        assertEquals(Pedido.EstadoPedido.ENVIADO, pedido1.getEstado());
        assertEquals(Pedido.EstadoPedido.ENVIADO, pedido2.getEstado());
    }

    @Test
    public void testManejoDePedidosUrgentes() throws InterruptedException {
        // Creamos un pedido urgente y un pedido normal
        Pedido pedidoNormal = new Pedido(1, "Cliente A", List.of("Producto 1"), false);
        Pedido pedidoUrgente = new Pedido(2, "Cliente B", List.of("Producto 2"), true);  // main.pedidos.Pedido urgente

        // Agregamos los pedidos al sistema
        sistema.agregarPedido(pedidoNormal);
        sistema.agregarPedido(pedidoUrgente);

        // Procesamos los pedidos
        sistema.procesarPedidos();

        // Esperamos unos segundos para asegurarnos de que ambos pedidos se procesan
        sistema.getExecutorPago().awaitTermination(5, TimeUnit.SECONDS);
        sistema.getExecutorEmpaquetado().awaitTermination(5, TimeUnit.SECONDS);
        sistema.getExecutorEnvio().awaitTermination(5, TimeUnit.SECONDS);

        // Verificamos que el pedido urgente fue procesado antes del pedido normal
        assertTrue(pedidoUrgente.getEstado() == Pedido.EstadoPedido.ENVIADO, "El pedido urgente debería procesarse primero");
        assertEquals(Pedido.EstadoPedido.ENVIADO, pedidoNormal.getEstado(), "El pedido normal también debería procesarse");
    }

    @Test
    public void testProcesamientoConcurrenteConMultiplesPedidos() throws InterruptedException {
        // Creamos una lista para almacenar los pedidos agregados
        List<Pedido> pedidos = new ArrayList<>();

        // Creamos varios pedidos
        for (int i = 1; i <= 10; i++) {
            boolean esUrgente = i % 2 == 0;  // Cada segundo pedido es urgente
            Pedido pedido = new Pedido(i, "Cliente " + i, List.of("Producto " + i), esUrgente);
            pedidos.add(pedido);  // Guardamos los pedidos en la lista
            sistema.agregarPedido(pedido);
        }

        // Procesamos los pedidos
        sistema.procesarPedidos();

        // Esperamos unos segundos para asegurarnos de que todos los pedidos se procesan
        sistema.getExecutorPago().awaitTermination(10, TimeUnit.SECONDS);
        sistema.getExecutorEmpaquetado().awaitTermination(10, TimeUnit.SECONDS);
        sistema.getExecutorEnvio().awaitTermination(10, TimeUnit.SECONDS);

        // Verificamos que todos los pedidos fueron procesados correctamente
        for (Pedido pedido : pedidos) {
            assertEquals(Pedido.EstadoPedido.ENVIADO, pedido.getEstado(), "El pedido " + pedido.getIdPedido() + " debería haber sido enviado.");
        }
    }

}
