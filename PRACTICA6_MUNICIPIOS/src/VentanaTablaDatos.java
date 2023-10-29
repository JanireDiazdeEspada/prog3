import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class VentanaTablaDatos extends JFrame {

	private static final int COL_AUTONOMIA = 4;
	
	private JTable tablaDatos;
	private DataSetMunicipios datosMunis;
	private MiTableModel modeloDatos;

	private String autonomiaSeleccionada = "";
	
	public VentanaTablaDatos( JFrame ventOrigen ) {
		setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		setSize( 800, 600 );
		setLocationRelativeTo( null );
		
		tablaDatos = new JTable();
		add( new JScrollPane( tablaDatos ), BorderLayout.CENTER );
		
		JPanel pInferior = new JPanel();
		JButton bAnyadir = new JButton( "Añadir" );
		JButton bBorrar = new JButton( "Borrar" );
		pInferior.add( bAnyadir );
		pInferior.add( bBorrar );
		add( pInferior, BorderLayout.SOUTH );
		
		this.addWindowListener( new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				ventOrigen.setVisible( false );
			}
			@Override
			public void windowClosed(WindowEvent e) {
				ventOrigen.setVisible( true );
			}
		});
	
		bBorrar.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int filaSel = tablaDatos.getSelectedRow();
				if (filaSel >= 0) {
					datosMunis.quitar( datosMunis.getListaMunicipios().get(filaSel).getCodigo() );
					modeloDatos.borraFila(filaSel);
				}
			}
		});
		
		bAnyadir.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int filaSel = tablaDatos.getSelectedRow();
				if (filaSel>=0) {
					datosMunis.anyadir( new Municipio( datosMunis.getListaMunicipios().size()+1, "Nombre", 0, "Provincia", "Autonomía" ), filaSel );
					modeloDatos.anyadeFila( filaSel );
				}
			}
		});
		
	}
	
	public void setDatos( DataSetMunicipios datosMunis ) {
		this.datosMunis = datosMunis;
		modeloDatos = new MiTableModel();
		tablaDatos.setModel( modeloDatos );
		
		TableColumn col = tablaDatos.getColumnModel().getColumn( 0 );
		col.setMaxWidth( 50 );
		col = tablaDatos.getColumnModel().getColumn(2);
		col.setMinWidth( 150 );
		col.setMaxWidth( 150 );

		tablaDatos.setDefaultRenderer( Integer.class, new DefaultTableCellRenderer() {
			private JProgressBar pbHabs = new JProgressBar( 0, 5000000 ) {
				protected void paintComponent(java.awt.Graphics g) {
					super.paintComponent(g);
					g.setColor( Color.BLACK );
					g.drawString( getValue()+"", 50, 10 );
				}
			};
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				if (column==2) {
					pbHabs.setValue( (Integer)value );
					return pbHabs;
				}
				JLabel rendPorDefecto = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				return rendPorDefecto;
			}
			
		});
		
		tablaDatos.setDefaultRenderer( String.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column );
				c.setBackground( Color.WHITE );
				if (isSelected) {  
					c.setBackground( Color.LIGHT_GRAY );
				}
				if (column==COL_AUTONOMIA) { 
					if (autonomiaSeleccionada.equals( (String)value )) {
						c.setBackground( Color.CYAN );
					}
				}
				return c;
			}
		} );
		
		tablaDatos.addMouseMotionListener( new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int filaEnTabla = tablaDatos.rowAtPoint( e.getPoint() );
				int colEnTabla = tablaDatos.columnAtPoint( e.getPoint() );
				if (colEnTabla == 2) {
					int numHabs = datosMunis.getListaMunicipios().get(filaEnTabla).getHabitantes();
					tablaDatos.setToolTipText( String.format( "Población: %,d", numHabs ) );
				} else {
					tablaDatos.setToolTipText( null );  
				}
			}
		});

		tablaDatos.addMouseListener( new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int filaEnTabla = tablaDatos.rowAtPoint( e.getPoint() );
				int colEnTabla = tablaDatos.columnAtPoint( e.getPoint() );
				if (colEnTabla == COL_AUTONOMIA && filaEnTabla>=0) {
					autonomiaSeleccionada = datosMunis.getListaMunicipios().get(filaEnTabla).getAutonomia();
				} else {
					autonomiaSeleccionada = "";
				}
				tablaDatos.repaint();
			}
		});

		tablaDatos.setDefaultEditor( Integer.class, new DefaultCellEditor( new JTextField() ) {
			SpinnerNumberModel mSpinner = new SpinnerNumberModel( 200000, 200000, 5000000, 1000 );
			JSpinner spinner = new JSpinner( mSpinner );
			boolean lanzadoSpinner;
			@Override
			public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) { // Componente que se pone en la tabla al editar una celda
				if (column != 2) {
					lanzadoSpinner = false;
					return super.getTableCellEditorComponent(table, value, isSelected, row, column);
				}
				mSpinner.setValue( (Integer) value );
				lanzadoSpinner = true;
				return spinner;
			}
			@Override
			public Object getCellEditorValue() { // Valor que se retorna al acabar la edición
				if (lanzadoSpinner) {
					return spinner.getValue();
				} else {
					return Integer.parseInt( super.getCellEditorValue().toString() );
				}
			}
		});
		
	}
	
	private class MiTableModel implements TableModel {

		private final Class<?>[] CLASES_COLS = { Integer.class, String.class, Integer.class, String.class, String.class };
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return CLASES_COLS[columnIndex]; 
		}

		@Override
		public int getColumnCount() {
			return 5;
		}

		@Override
		public int getRowCount() {
			return datosMunis.getListaMunicipios().size();
		}

		private final String[] cabeceras = { "Código", "Nombre", "Habitantes", "Provincia", "Autonomía" };
		@Override
		public String getColumnName(int columnIndex) {
			return cabeceras[columnIndex];
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return datosMunis.getListaMunicipios().get(rowIndex).getCodigo();
			case 1:
				return datosMunis.getListaMunicipios().get(rowIndex).getNombre();
			case 2:
				return datosMunis.getListaMunicipios().get(rowIndex).getHabitantes();
			case 3:
				return datosMunis.getListaMunicipios().get(rowIndex).getProvincia();
			case 4:
				return datosMunis.getListaMunicipios().get(rowIndex).getAutonomia();
			default:
				return null;
			}
		}

		@Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
			System.out.println( "isCellEditable" );
			if (columnIndex == 0) {
				return false;
			}
			return true;
		}

		@Override
		public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
			System.out.println( "setValue " + aValue + "[" + aValue.getClass().getName() + "] " + rowIndex + "," + columnIndex );
			switch (columnIndex) {
			case 0:
				datosMunis.getListaMunicipios().get(rowIndex).setCodigo( (Integer) aValue );
				break;
			case 1:
				datosMunis.getListaMunicipios().get(rowIndex).setNombre( (String) aValue );
				break;
			case 2:
				try {
					datosMunis.getListaMunicipios().get(rowIndex).setHabitantes( (Integer) aValue );
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog( VentanaTablaDatos.this, "Nº de habitantes erróneo" );
				}
				break;
			case 3:
				datosMunis.getListaMunicipios().get(rowIndex).setProvincia( (String) aValue );
				break;
			case 4:
				datosMunis.getListaMunicipios().get(rowIndex).setAutonomia( (String) aValue );
				break;
			}
		}

		ArrayList<TableModelListener> listaEsc = new ArrayList<>();
		@Override
		public void addTableModelListener(TableModelListener l) {
			System.out.println( "addTableModelListener" );
			listaEsc.add( l );
		}

		@Override
		public void removeTableModelListener(TableModelListener l) {
			listaEsc.remove( l );
		}
		
		public void fireTableChanged( TableModelEvent e ) {
			for (TableModelListener l : listaEsc) {
				l.tableChanged( e );
			}
		}
		
		public void borraFila( int fila ) {
			fireTableChanged( new TableModelEvent( modeloDatos, fila, datosMunis.getListaMunicipios().size() ));
		}
		
	    public void anyadeFila( int fila ) {
	    	fireTableChanged( new TableModelEvent( modeloDatos, fila, datosMunis.getListaMunicipios().size() ) );  // Para que detecte el cambio en todas
	    }
	    
	}
}
