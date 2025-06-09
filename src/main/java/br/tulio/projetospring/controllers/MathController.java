package br.tulio.projetospring.controllers;

import br.tulio.projetospring.exception.ResourceNotFoundException;
import br.tulio.projetospring.tools.NumericConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/math")
public class MathController {

    //http://localhost:8080/math/sum/1/2
    @RequestMapping("/sum/{num1}/{num2}")
    public final Double sum(@PathVariable("num1") String num1, @PathVariable("num2") String num2) throws ResourceNotFoundException
    {
        if(!NumericConverter.isNumeric(num1) || !NumericConverter.isNumeric(num2)) throw new ResourceNotFoundException("Error: Please set a valid numeric value");

        return NumericConverter.convertToDouble(num1) + NumericConverter.convertToDouble(num2);
    }

    //http://localhost:8080/math/sum/1/2
    @RequestMapping("/mult/{num1}/{num2}")
    public final Double multiply(@PathVariable("num1") String num1, @PathVariable("num2") String num2)
    {
        if(!NumericConverter.isNumeric(num1) || !NumericConverter.isNumeric(num2)) throw new ResourceNotFoundException("Error: Please set a valid numeric value");

        return NumericConverter.convertToDouble(num1) * NumericConverter.convertToDouble(num2);
    }

    @RequestMapping("/div/{num1}/{num2}")
    public final Double divide(@PathVariable("num1") String num1, @PathVariable("num2") String num2)
    {
        if(!NumericConverter.isNumeric(num1) || !NumericConverter.isNumeric(num2)) throw new ResourceNotFoundException("Error: Please set a valid numeric value");

        return NumericConverter.convertToDouble(num1) / NumericConverter.convertToDouble(num2);
    }

    @RequestMapping("/mean/{num1}/{num2}")
    public final Double mean(@PathVariable("num1") String num1, @PathVariable("num2") String num2)
    {
        if(!NumericConverter.isNumeric(num1) || !NumericConverter.isNumeric(num2)) throw new ResourceNotFoundException("Error: Please set a valid numeric value");

        return (NumericConverter.convertToDouble(num1) + NumericConverter.convertToDouble(num2) ) / 2;
    }

    @RequestMapping("/sqrt/{num1}")
    public final Double squaredRoot(@PathVariable("num1") String num1)
    {
        if(!NumericConverter.isNumeric(num1) ) throw new ResourceNotFoundException("Error: Please set a valid numeric value");

        return Math.sqrt(NumericConverter.convertToDouble(num1));
    }




}
