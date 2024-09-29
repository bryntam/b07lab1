import java.io.*;
import java.util.ArrayList;

public class Polynomial {
    private double[] coefficients;  
    private int[] exponents;        

    public Polynomial() {
        this.coefficients = new double[] {0};
        this.exponents = new int[] {0};
    }

    public Polynomial(double[] coefficients, int[] exponents) {
        this.coefficients = coefficients;
        this.exponents = exponents;
    }

    public Polynomial(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        reader.close();

        ArrayList<Double> coeffList = new ArrayList<>();
        ArrayList<Integer> expList = new ArrayList<>();

        String[] terms = line.split("(?=[+-])");

        for (String term : terms) {
            if (term.contains("x")) {
                String[] parts = term.split("x");
                double coefficient = parts[0].equals("") || parts[0].equals("+") ? 1 :
                        parts[0].equals("-") ? -1 : Double.parseDouble(parts[0]);
                int exponent = parts.length == 1 ? 1 : Integer.parseInt(parts[1].replace("^", ""));
                coeffList.add(coefficient);
                expList.add(exponent);
            } else {
                coeffList.add(Double.parseDouble(term));
                expList.add(0);  
            }
        }

        this.coefficients = new double[coeffList.size()];
        this.exponents = new int[expList.size()];
        for (int i = 0; i < coeffList.size(); i++) {
            this.coefficients[i] = coeffList.get(i);
            this.exponents[i] = expList.get(i);
        }
    }

    public Polynomial add(Polynomial other) {
        ArrayList<Double> resultCoeffs = new ArrayList<>();
        ArrayList<Integer> resultExps = new ArrayList<>();

        int i = 0, j = 0;
        while (i < this.coefficients.length || j < other.coefficients.length) {
            int exp1 = i < this.exponents.length ? this.exponents[i] : Integer.MIN_VALUE;
            int exp2 = j < other.exponents.length ? other.exponents[j] : Integer.MIN_VALUE;

            if (exp1 == exp2) {
                resultCoeffs.add(this.coefficients[i] + other.coefficients[j]);
                resultExps.add(exp1);
                i++;
                j++;
            } else if (exp1 > exp2) {
                resultCoeffs.add(this.coefficients[i]);
                resultExps.add(exp1);
                i++;
            } else {
                resultCoeffs.add(other.coefficients[j]);
                resultExps.add(exp2);
                j++;
            }
        }

        double[] finalCoeffs = new double[resultCoeffs.size()];
        int[] finalExps = new int[resultExps.size()];
        for (int k = 0; k < resultCoeffs.size(); k++) {
            finalCoeffs[k] = resultCoeffs.get(k);
            finalExps[k] = resultExps.get(k);
        }

        return new Polynomial(finalCoeffs, finalExps);
    }

    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, exponents[i]);
        }
        return result;
    }

    public Polynomial multiply(Polynomial other) {
        ArrayList<Double> resultCoeffs = new ArrayList<>();
        ArrayList<Integer> resultExps = new ArrayList<>();

        for (int i = 0; i < this.coefficients.length; i++) {
            for (int j = 0; j < other.coefficients.length; j++) {
                double newCoeff = this.coefficients[i] * other.coefficients[j];
                int newExp = this.exponents[i] + other.exponents[j];
                int index = resultExps.indexOf(newExp);
                if (index != -1) {
                    resultCoeffs.set(index, resultCoeffs.get(index) + newCoeff);
                } else {
                    resultCoeffs.add(newCoeff);
                    resultExps.add(newExp);
                }
            }
        }

        double[] finalCoeffs = new double[resultCoeffs.size()];
        int[] finalExps = new int[resultExps.size()];
        for (int k = 0; k < resultCoeffs.size(); k++) {
            finalCoeffs[k] = resultCoeffs.get(k);
            finalExps[k] = resultExps.get(k);
        }

        return new Polynomial(finalCoeffs, finalExps);
    }

    public void saveToFile(String filename) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < coefficients.length; i++) {
            double coeff = coefficients[i];
            int exp = exponents[i];

            if (i != 0 && coeff > 0) {
                sb.append("+");
            }

            if (coeff == 1 && exp != 0) {
                sb.append("x");
            } else if (coeff == -1 && exp != 0) {
                sb.append("-x");
            } else {
                sb.append(coeff);
            }

            if (exp != 0) {
                sb.append("^").append(exp);
            }
        }

        writer.write(sb.toString());
        writer.close();
    }

    public void printPolynomial() {
        for (int i = 0; i < coefficients.length; i++) {
            if (i > 0 && coefficients[i] >= 0) {
                System.out.print("+");
            }
            System.out.print(coefficients[i]);
            if (exponents[i] != 0) {
                System.out.print("x^" + exponents[i]);
            }
        }
        System.out.println();
    }

}
