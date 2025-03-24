/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 *
 * @author Janidu
 */
public class CustomTable {

    public void modifyLayout(JTable table) {

//        table.
        // Customize row height
        table.setRowHeight(35);

        // Customize column width (for example, set the width of the first column)
        table.getColumnModel().getColumn(0).setWidth(50);

        // Customize row font
        Font rowFont = new Font("Roboto", Font.PLAIN, 18);
        table.setFont(rowFont);

        // Customize header font
        JTableHeader header = table.getTableHeader();
        Font headerFont = new Font("Roboto", Font.PLAIN, 20);
        header.setFont(headerFont);

        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 40));
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        table.getTableHeader().setDefaultRenderer(centerRenderer);

        // Create a custom cell renderer to set row colors
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Set row color when selected
                if (isSelected) {
                    c.setBackground(new Color(102, 0, 0));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }

                return c;
            }
        };
        cellRenderer.setHorizontalAlignment(JLabel.CENTER);
        // Apply the custom cell renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        // Add a list selection listener to change row colors on selection change
        ListSelectionModel selectionModel = table.getSelectionModel();

        // Set the selection mode to SINGLE_SELECTION
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int[] selectedRows = table.getSelectedRows();
                table.repaint();

                // You can perform additional actions based on the selected rows if needed
            }
        });
    }

    public void modifyLayout(JTable table, int alignment) {

//        table.
        // Customize row height
        table.setRowHeight(35);

        // Customize column width (for example, set the width of the first column)
        table.getColumnModel().getColumn(0).setWidth(50);

        table.getTableHeader().setPreferredSize(new Dimension(table.getTableHeader().getWidth(), 40));
        DefaultTableCellRenderer centerRenderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();

        centerRenderer.setHorizontalAlignment(alignment);

        table.getTableHeader().setDefaultRenderer(centerRenderer);
        
        // Customize header font
        JTableHeader header = table.getTableHeader();
        Font headerFont = new Font("Roboto", Font.PLAIN, 20);
        header.setFont(headerFont);

        // Customize row font
        Font rowFont = new Font("Roboto", Font.PLAIN, 18);
        table.setFont(rowFont);

        // Create a custom cell renderer to set row colors
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Set row color when selected
                if (isSelected) {
                    c.setBackground(new Color(102, 0, 0));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(table.getBackground());
                    c.setForeground(table.getForeground());
                }

                return c;
            }
        };
        cellRenderer.setHorizontalAlignment(alignment);
        // Apply the custom cell renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
        // Add a list selection listener to change row colors on selection change
        ListSelectionModel selectionModel = table.getSelectionModel();

        // Set the selection mode to SINGLE_SELECTION
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int[] selectedRows = table.getSelectedRows();
                table.repaint();

                // You can perform additional actions based on the selected rows if needed
            }
        });

    }
}
