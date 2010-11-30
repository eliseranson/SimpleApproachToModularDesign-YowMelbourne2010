package ca.jbrains.pos.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class SellOneItemTest {
    public static class Sale {
        private Display display;
        private Map<String, String> pricesByBarcode;

        public Sale(Display display, Map<String, String> pricesByBarcode) {
            this.display = display;
            this.pricesByBarcode = pricesByBarcode;
        }

        public void onBarcode(String barcode) {
            if ("".equals(barcode)) {
                display.displayEmptyBarcodeMessage();
                return;
            }

            if (pricesByBarcode.containsKey(barcode))
                display.displayPrice(pricesByBarcode.get(barcode));
            else
                display.displayProductNotFoundMessage(barcode);
        }

    }

    public static class Display {
        private String text;

        public void setText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void displayEmptyBarcodeMessage() {
            setText("Scanning error: empty barcode");
        }

        public void displayProductNotFoundMessage(String barcode) {
            setText("No product with barcode " + barcode);
        }

        public void displayPrice(String price) {
            setText(price);
        }
    }

    @Test
    public void productFound() throws Exception {
        Display display = new Display();
        Sale sale = new Sale(display, new HashMap<String, String>() {
            {
                put("123", "$12.50");
                put("456", "$20.00");
            }
        });

        sale.onBarcode("123");

        assertEquals("$12.50", display.getText());
    }

    @Test
    public void anotherProductFound() throws Exception {
        Display display = new Display();
        Sale sale = new Sale(display, new HashMap<String, String>() {
            {
                put("123", "$20.00");
            }
        });

        sale.onBarcode("123");

        assertEquals("$20.00", display.getText());
    }

    @Test
    public void productNotFound() throws Exception {
        Display display = new Display();
        Sale sale = new Sale(display, new HashMap<String, String>() {
            {
                put("123", "$12.50");
                put("456", "$20.00");
            }
        });

        sale.onBarcode("999");

        assertEquals("No product with barcode 999", display.getText());
    }

    @Test
    public void emptyBarcode() throws Exception {
        Display display = new Display();
        Sale sale = new Sale(display, new HashMap<String, String>() {
            {
                put("123", "$12.50");
                put("456", "$20.00");
            }
        });

        sale.onBarcode("");

        assertEquals("Scanning error: empty barcode", display.getText());
    }
}
