/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tiendapedidosmultiflujo;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

/**
 *
 * @author VITG00M
 */
public class TiendaPedidosMultiflujo extends JFrame {

    private CardLayout cardLayout;
    private JPanel contenedorPrincipal;

    // Componentes del módulo Generación de Comprobantes
    private JTextField txtFolioPedido;
    private JTextArea txtAreaProgreso;
    private JButton btnIniciar;
    private JButton btnRegresar;

    public TiendaPedidosMultiflujo() {
        // Barra de título con módulo asignado y nombre completo
        setTitle("Generación de comprobantes - German Olvera Tejero");
        setSize(650, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        contenedorPrincipal = new JPanel(cardLayout);

        contenedorPrincipal.add(crearPanelMenuPrincipal(), "MENU");
        contenedorPrincipal.add(crearPanelModuloComprobantes(), "MODULO");

        add(contenedorPrincipal);
        cardLayout.show(contenedorPrincipal, "MENU");
    }

    // 1. Pantalla de inicio y Menú Principal
    private JPanel crearPanelMenuPrincipal() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(245, 247, 250));

        // Encabezado
        JLabel lblTitulo = new JLabel("Tienda en Línea - Sistema Multi-flujo", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(27, 54, 93));
        panel.add(lblTitulo, BorderLayout.NORTH);

        // Centro: Imagen alusiva + datos del módulo
        JPanel panelCentro = new JPanel();
        panelCentro.setLayout(new BoxLayout(panelCentro, BoxLayout.Y_AXIS));
        panelCentro.setOpaque(false);

        JLabel lblImagen = new JLabel();
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        try {
            ImageIcon icono = new ImageIcon(getClass().getResource("/imagenes/comprobante.png"));
            Image imgEscalada = icono.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            lblImagen.setIcon(new ImageIcon(imgEscalada));
        } catch (Exception e) {
            lblImagen.setText("[Módulo: Generación de Comprobantes]");
            lblImagen.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        }

        JLabel lblModulo = new JLabel("Módulo Asignado: Generación de comprobantes (M - P)");
        lblModulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblModulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEstudiante = new JLabel("Desarrollado por: German Olvera Tejero | Matrícula: ES172000419");
        lblEstudiante.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblEstudiante.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelCentro.add(Box.createVerticalStrut(15));
        panelCentro.add(lblImagen);
        panelCentro.add(Box.createVerticalStrut(15));
        panelCentro.add(lblModulo);
        panelCentro.add(Box.createVerticalStrut(8));
        panelCentro.add(lblEstudiante);

        panel.add(panelCentro, BorderLayout.CENTER);

        // Botones del Menú Principal
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setOpaque(false);

        JButton btnAcceder = new JButton("Generación de comprobantes");
        btnAcceder.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAcceder.setBackground(new Color(27, 54, 93));
        btnAcceder.setForeground(Color.WHITE);
        btnAcceder.addActionListener(e -> cardLayout.show(contenedorPrincipal, "MODULO"));

        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(btnAcceder);
        panelBotones.add(btnSalir);
        panel.add(panelBotones, BorderLayout.SOUTH);

        return panel;
    }

    // 2. Formulario del módulo asignado
    private JPanel crearPanelModuloComprobantes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panel.setBackground(Color.WHITE);

        // Parte superior: Captura de datos
        JPanel panelCaptura = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelCaptura.setBorder(BorderFactory.createTitledBorder("Datos del Pedido para Comprobante"));
        panelCaptura.setBackground(Color.WHITE);

        JLabel lblDato = new JLabel("Folio del Pedido / Cliente:");
        lblDato.setFont(new Font("Segoe UI", Font.BOLD, 12));

        txtFolioPedido = new JTextField(22);
        txtFolioPedido.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        btnIniciar = new JButton("Iniciar");
        btnIniciar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnIniciar.setBackground(new Color(22, 101, 52));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.addActionListener(e -> iniciarProcesoEnHilo());

        panelCaptura.add(lblDato);
        panelCaptura.add(txtFolioPedido);
        panelCaptura.add(btnIniciar);

        panel.add(panelCaptura, BorderLayout.NORTH);

        // Parte central: Bitácora de ejecución del hilo
        txtAreaProgreso = new JTextArea();
        txtAreaProgreso.setEditable(false);
        txtAreaProgreso.setFont(new Font("Consolas", Font.PLAIN, 12));
        txtAreaProgreso.setBackground(new Color(248, 250, 252));
        JScrollPane scroll = new JScrollPane(txtAreaProgreso);
        scroll.setBorder(BorderFactory.createTitledBorder("Monitor de Ejecución del Hilo (Thread)"));
        panel.add(scroll, BorderLayout.CENTER);

        // Parte inferior: Botón regresar al menú principal
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelSur.setBackground(Color.WHITE);
        btnRegresar = new JButton("Regresar al Menú Principal");
        btnRegresar.addActionListener(e -> {
            txtAreaProgreso.setText("");
            txtFolioPedido.setText("");
            cardLayout.show(contenedorPrincipal, "MENU");
        });
        panelSur.add(btnRegresar);
        panel.add(panelSur, BorderLayout.SOUTH);

        return panel;
    }

    // 3. Ejecución mediante hilo independiente (Runnable + Thread.sleep)
    private void iniciarProcesoEnHilo() {
        String datoPedido = txtFolioPedido.getText().trim();

        // Validación de campo vacío
        if (datoPedido.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Por favor, captura el folio o datos del pedido antes de iniciar.",
                    "Campo obligatorio vacío",
                    JOptionPane.WARNING_MESSAGE
            );
            txtFolioPedido.requestFocus();
            return;
        }

        btnIniciar.setEnabled(false);
        btnRegresar.setEnabled(false);
        txtAreaProgreso.setText("");

        // Creación del hilo secundario para no bloquear el hilo gráfico (EDT)
        Runnable tareaComprobante = () -> {
            String nombreHilo = Thread.currentThread().getName();
            String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            try {
                agregarMensaje("[INICIO - " + nombreHilo + "] Iniciando generación de comprobante para: " + datoPedido);
                Thread.sleep(1200);

                agregarMensaje("[DESARROLLO - " + nombreHilo + "] Recuperando detalles fiscales e importes del pedido...");
                Thread.sleep(1500);

                agregarMensaje("[DESARROLLO - " + nombreHilo + "] Asignando folio digital y sello de validación...");
                Thread.sleep(1500);

                agregarMensaje("[DESARROLLO - " + nombreHilo + "] Escribiendo archivo físico resultado.txt...");
                guardarArchivoResultado(datoPedido, fechaHora, nombreHilo);
                Thread.sleep(1000);

                agregarMensaje("[FINALIZACIÓN - " + nombreHilo + "] Comprobante generado y guardado en resultado.txt con éxito.");

            } catch (InterruptedException ex) {
                agregarMensaje("[ERROR] El hilo fue interrumpido: " + ex.getMessage());
                Thread.currentThread().interrupt();
            } catch (IOException ex) {
                agregarMensaje("[ERROR DE ARCHIVO] No se pudo escribir resultado.txt: " + ex.getMessage());
            } finally {
                SwingUtilities.invokeLater(() -> {
                    btnIniciar.setEnabled(true);
                    btnRegresar.setEnabled(true);
                });
            }
        };

        Thread hiloComprobante = new Thread(tareaComprobante, "Hilo-Comprobantes-01");
        hiloComprobante.start();
    }

    private void agregarMensaje(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            txtAreaProgreso.append(mensaje + "\n");
            txtAreaProgreso.setCaretPosition(txtAreaProgreso.getDocument().getLength());
        });
    }

    // 4. Generación del archivo resultado.txt
    private void guardarArchivoResultado(String datoPedido, String fechaHora, String nombreHilo) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resultado.txt", true))) {
            writer.write("======================================================");
            writer.newLine();
            writer.write("TIENDA EN LÍNEA - COMPROBANTE DE COMPRA ELECTRÓNICO");
            writer.newLine();
            writer.write("Módulo: Generación de comprobantes (Etapa Multi-flujo)");
            writer.newLine();
            writer.write("Estudiante: German Olvera Tejero (ES172000419)");
            writer.newLine();
            writer.write("Pedido / Cliente procesado: " + datoPedido);
            writer.newLine();
            writer.write("Fecha y hora de emisión: " + fechaHora);
            writer.newLine();
            writer.write("Procesado por hilo: " + nombreHilo);
            writer.newLine();
            writer.write("Estado: COMPROBANTE EMITIDO CORRECTAMENTE");
            writer.newLine();
            writer.write("======================================================");
            writer.newLine();
            writer.newLine();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TiendaPedidosMultiflujo().setVisible(true);
        });
    }
}