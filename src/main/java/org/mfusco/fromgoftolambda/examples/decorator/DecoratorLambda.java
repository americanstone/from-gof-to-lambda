package org.mfusco.fromgoftolambda.examples.decorator;

import java.util.function.DoubleUnaryOperator;
import java.util.stream.Stream;

public class DecoratorLambda {

    public static class DefaultSalaryCalculator implements DoubleUnaryOperator {
        @Override
        public double applyAsDouble( double grossAnnual ) {
            return grossAnnual / 12;
        }
    }
 //the context is we have some fixed set of operations the api consumer can choose all of some of them to execute
// compare to template pattern, template is set of operations all of them have to be executed in particular orders
// compare to chain of responsibility, it's set of operations executed in certain oder if pass current stage check.
    public static void main( String[] args ) {
        new DefaultSalaryCalculator()
                .andThen( Taxes::generalTax )
                .andThen( Taxes::regionalTax )
                .andThen( Taxes::healthInsurance )
                .applyAsDouble( 80000.00 );

        calculateSalary( 80000.00, new DefaultSalaryCalculator(), Taxes::generalTax, Taxes::regionalTax, Taxes::healthInsurance );
    }

    public static double calculateSalary(double annualGross, DoubleUnaryOperator... taxes) {
        return Stream.of(taxes).reduce( DoubleUnaryOperator.identity(), DoubleUnaryOperator::andThen ).applyAsDouble( annualGross );
    }
}
