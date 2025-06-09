package br.tulio.projetospring.tools;

import br.tulio.projetospring.exception.UnsuportedMathOperationException;

public class NumericConverter {

    public static Double convertToDouble(String value) throws UnsuportedMathOperationException {

        if(value.isEmpty()) throw new UnsuportedMathOperationException("Error: Please set a valid numeric value");

        String number = value.replace(",", ".");

        return Double.parseDouble(number);
    }

    public static boolean isNumeric(String value) {

        if(value.isEmpty()) return false;

        String number = value.replace(",", ".");

        return number.matches("[+-]?[0-9]*\\.?[0-9]+");
    }
}
