import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class VentanaPr6 extends JFrame{
	private JPanel pNorte, pIzquierda, pCentro, pDerecha, pBotonera;
	private JLabel lMensaje;
	private DefaultTreeModel modeloArbol;
	private JTree arbol;
	private JScrollPane scrollArbol,scrollTabla;
	private DefaultTableModel modeloTabla;
	private JTable tabla;
	private JButton btnInsertar, btnBorrar, btnOrden;
	private DataSetMunicipios dsm;
	private int filaSeleccionada,columnaSeleccionada;
	private boolean orden;
	private String provinciaSeleccionada;
	
	public VentanaPr6() {
		super();
		setBounds(300, 200, 600, 400);
		filaSeleccionada = -1;
		columnaSeleccionada = -1;
		orden = false;
		pNorte = new JPanel();
		pIzquierda = new JPanel();
		pDerecha = new JPanel();
		pCentro = new JPanel();
		pBotonera = new JPanel();
		getContentPane().add(pNorte,BorderLayout.NORTH);
		getContentPane().add(pIzquierda, BorderLayout.WEST);
		getContentPane().add(pDerecha, BorderLayout.EAST);
		getContentPane().add(pCentro, BorderLayout.CENTER);
		getContentPane().add(pBotonera, BorderLayout.SOUTH);
		
		lMensaje = new JLabel();
		pNorte.add(lMensaje);
		
		modeloArbol = new DefaultTreeModel(new DefaultMutableTreeNode("Municipios"));
		arbol = new JTree(modeloArbol);
		arbol.setEditable(false);
		scrollArbol = new JScrollPane(arbol);
		pIzquierda.add(scrollArbol);
		
		String [] titulos = {"MUNICIPIO","HABITANTES","PORCENTAJE","PROVINCIA","COMUNIDAD"};
		modeloTabla = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				if(column<=1)
					return true;
				return false;
			}
		};
		modeloTabla.setColumnIdentifiers(titulos);
		tabla = new JTable(modeloTabla);
		scrollTabla = new JScrollPane(tabla);
		pCentro.add(scrollTabla);
		
		btnInsertar = new JButton("INSERCIÓN");
		btnBorrar = new JButton("BORRADO");
		btnOrden = new JButton("ORDEN");
		pBotonera.add(btnInsertar);
		pBotonera.add(btnBorrar);
		pBotonera.add(btnOrden);
		
		try {
			dsm = new DataSetMunicipios("municipios200k.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cargarJTree();
		
		arbol.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				while(modeloTabla.getRowCount()>0) {
					modeloTabla.removeRow(0);
				}
				TreePath tp = e.getPath();
				provinciaSeleccionada = tp.getLastPathComponent().toString();
				cargarTabla();
			}
		});
		
		tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if(column==2) {
					JProgressBar pb =  new JProgressBar(50000, 500000);
					pb.setValue(Integer.parseInt(value.toString()));
					pb.setStringPainted(true); 
					pb.setBackground(Color.GREEN); 
					pb.setForeground(Color.RED); 
					return pb;
				}else {
					return c;
				}
			}
		});
		
		tabla.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Point point = e.getPoint();
				filaSeleccionada = tabla.rowAtPoint(point);
				columnaSeleccionada = tabla.columnAtPoint(point);
			}
		});
		
		btnInsertar.addActionListener((e)->{
			String provincia = "";
			if(filaSeleccionada!=-1)
				provincia = modeloTabla.getValueAt(filaSeleccionada, 3).toString();
			String comunidad = "";
			if(filaSeleccionada!=-1)
				comunidad = modeloTabla.getValueAt(filaSeleccionada, 4).toString();
			
			Object [] fila = {"",50000,50000,provincia,comunidad};
			modeloTabla.addRow(fila);
		});
		
		btnBorrar.addActionListener((e)->{
			int fila = tabla.getSelectedRow();
			if(fila!=-1) {
				int resp = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres borrar la fila seleccionada?");
				if(resp==0) {
					modeloTabla.removeRow(fila);
				}
			}
		});
		
		btnOrden.addActionListener((e)->{
			List<Municipio> l = dsm.getmMunicipiosOrdenados().get(provinciaSeleccionada);
			if(!orden) {
				Comparator<Municipio> c = new Comparator<Municipio>() {
					@Override
					public int compare(Municipio o1, Municipio o2) {
						return o1.getHabitantes() - o2.getHabitantes();
					}
				};
				Collections.sort(l, c);
				orden = true;
			}else {
				Comparator<Municipio> c = new Comparator<Municipio>() {
					@Override
					public int compare(Municipio o1, Municipio o2) {
						return o1.getNombre().compareTo(o2.getNombre());
					}
				};
				Collections.sort(l, c);
				orden = false;  
			}
			while(modeloTabla.getRowCount()>0) {
				modeloTabla.removeRow(0);
			}
			for(Object o: l) {
				Municipio m = (Municipio)o;
				Object [] fila = {m.getNombre(),m.getHabitantes(),m.getHabitantes(),m.getProvincia(),m.getAutonomia()};
				modeloTabla.addRow(fila);
			}
			tabla.repaint();
		});
		setVisible(true);
	}
	
	private void cargarJTree() {
		int i=0;
		for(String c: dsm.getmMunicipios().keySet()) {
			DefaultMutableTreeNode dmtn = new DefaultMutableTreeNode(c);
			int j=0;
			for(String p: dsm.getmMunicipios().get(c)) {
				DefaultMutableTreeNode dmtn2 = new DefaultMutableTreeNode(p);
				modeloArbol.insertNodeInto(dmtn2, dmtn, j++);
			}
			modeloArbol.insertNodeInto(dmtn, (DefaultMutableTreeNode)modeloArbol.getRoot(), i++);
			
		}
	}
	
	private void cargarTabla() {
		if(dsm.getmMunicipiosOrdenados().containsKey(provinciaSeleccionada)) {
			for(Municipio m: dsm.getmMunicipiosOrdenados().get(provinciaSeleccionada)) {
				Object [] fila = {m.getNombre(),m.getHabitantes(),m.getHabitantes(),m.getProvincia(),m.getAutonomia()};
				modeloTabla.addRow(fila);
			}
		}
		tabla.repaint();	
	}
	public static void main(String[] args) {
		VentanaPr6 v = new VentanaPr6();
	}
}
