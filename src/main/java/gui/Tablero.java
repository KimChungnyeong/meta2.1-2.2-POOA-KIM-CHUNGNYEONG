package gui;

import kanvan.Actividad;
import kanvan.Fase;
import kanvan.FlujoTrabajo;
import kanvan.Tarea;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;

public class Tablero extends JDialog {
    kanvan.FlujoTrabajo flujoTrabajo;
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton botonAddActividad;
    private JButton botonAddFase;
    private JButton botonAddTarea;
    private JTextField textFieldActividad;
    private JTextField textFieldFase;
    private JTextField textFieldTarea;
    private JComboBox comboBoxActividad;
    private JComboBox comboBoxFase;
    private JTable tablero;
    private JButton buttonSalir;
    private JButton buttonBorraFila;
    private DefaultTableModel modelo;

    public Tablero() {
        flujoTrabajo=new FlujoTrabajo("flujo de trabajo");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        botonAddActividad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Actividad actividad = new Actividad(textFieldActividad.getText(),flujoTrabajo);
                flujoTrabajo.getActividad().add(actividad);
                actualizarTablero();

            }
        });
        botonAddFase.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Fase fase = new Fase(textFieldFase.getText(),flujoTrabajo);
                flujoTrabajo.getFase().add(fase);
                actualizarTablero();

            }
        });
        botonAddTarea.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Actividad actividad = flujoTrabajo.getActividad().get(comboBoxActividad.getSelectedIndex());
                Fase fase = flujoTrabajo.getFase().get(comboBoxFase.getSelectedIndex());

                Tarea tarea = new Tarea(textFieldTarea.getText(),flujoTrabajo,actividad,fase);
                actividad.getTarea().add(tarea);
                fase.getTarea().add(tarea);
                flujoTrabajo.getTarea().add(tarea);

                actualizarTablero();
                //JOptionPane.showMessageDialog(null,flujoTrabajo);
            }
        });
        buttonSalir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonBorraFila.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int fila,ban=0;
                try {
                    fila= tablero.getSelectedRow();
                    if(fila==-1)
                    {
                        JOptionPane.showMessageDialog(null,"Elegir una fila a borrar");
                    }
                    else
                        ban=JOptionPane.showConfirmDialog(null,"Esta seguro que lo quiere borrar?");
                        if(ban==JOptionPane.YES_OPTION)
                        {
                            Fase fase = flujoTrabajo.getTarea().get(comboBoxActividad.getSelectedIndex()).getFase();
                            Actividad actividad = flujoTrabajo.getTarea().get(comboBoxActividad.getSelectedIndex()).getActividad();
                            Tarea tarea = flujoTrabajo.getTarea().get(comboBoxActividad.getSelectedIndex());

                            flujoTrabajo.getTarea().removeElement(tarea);
                            fase.getTarea().removeElement(tarea);
                            actividad.getTarea().remove(tarea);
                            actualizarTablero();
                        }
                }catch(Exception f){
                }
            }
        });
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void  actualizarTablero() {
        modelo=new DefaultTableModel();;
        tablero.setModel(modelo);


        comboBoxActividad.removeAllItems();
        for(int i=0;i<flujoTrabajo.getActividad().size();i++)
        {
            comboBoxActividad.addItem(flujoTrabajo.getActividad().get(i).getNombre());
        }

        comboBoxFase.removeAllItems();
        for(int i=0;i<flujoTrabajo.getFase().size();i++)
        {
            comboBoxFase.addItem(flujoTrabajo.getFase().get(i).getNombre());
        }

        for(int i=0;i<flujoTrabajo.getFase().size();i++)
        {
            modelo.addColumn(flujoTrabajo.getFase().get(i).getNombre(),flujoTrabajo.getFase().get(i).getTarea());
        }
    }
    public static void main(String[] args) {
        Tablero dialog = new Tablero();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
