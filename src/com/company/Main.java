package com.company;
import java.util.LinkedList;
import java.util.Scanner;

public class Main {

    static boolean isSpace(char c) { // игнорирование пробелов в выражении
        return c == ' ';
    }
    static boolean isOperator(char c)
    { // определение знака операции
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == 's' || c == 'c' || c == 't' || c == '#';
    }
    static int priority(char op)
    {
        switch (op) { // определение приоритета знака операции
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
            case 's':
            case 'c':
            case 't':
            case '#':
                return 2;
            default:
                return -1;
        }
    }
    static void processOperator(LinkedList<Double> st, char op, double results[]) throws Exception {
        if ((op > 96) & (op < 123)||(op == '#')) { //ascii код символа или оператор номер строки
            double operand = st.removeLast();
            double radians = Math.toRadians(operand);
            switch (op) { // выполнение действия между операндами и результат записывается в st
                case 's':
                    st.add(Math.sin(radians));
                    break;
                case 'c':
                    st.add(Math.cos(radians));
                    break;
                case 't':
                    st.add(Math.tan(radians));
                    break;
                case '#':
                    int index = (int)operand;
                    st.add(results[index-1]);
                    break;
                default:
                    throw new Exception("Недопустимая операция " + op);
            }
        }
        else {
            double r_operand = st.removeLast(); // получение правого операнда
            double l_operand = st.removeLast(); // получение левого операнда
            switch (op) { // выполнение действия между операндами и результат записывается в st
                case '+':
                    st.add(l_operand + r_operand);
                    break;
                case '-':
                    st.add(l_operand - r_operand);
                    break;
                case '*':
                    st.add(l_operand * r_operand);
                    break;
                case '/':
                    st.add(l_operand / r_operand);
                    break;
                case '%':
                    st.add(l_operand % r_operand);
                    break;
                default:
                    throw new Exception("Недопустимая операция " + op);
            }
        }
    }

    public static Double eval(String s, double results[])
    {
        try
        {
            LinkedList<Double> st = new LinkedList<Double>(); // контейнер для операндов в порядке поступления
            LinkedList<Character> op = new LinkedList<Character>(); // контейнер для знаков операции в порядке поступления
            for (int i = 0; i < s.length(); i++) { // парсинг формулы
                char c = s.charAt(i);
                if (isSpace(c))
                    continue;
                if (c == '(')
                    op.add('(');
                else if (c == ')')
                {
                    while (op.getLast() != '(')
                        processOperator(st, op.removeLast(), results);
                    op.removeLast();
                }
                else if (isOperator(c))
                {
                    while (!op.isEmpty() && priority(op.getLast()) >= priority(c))
                        processOperator(st, op.removeLast(), results);
                    op.add(c);
                }
                else
                {
                        String operand = "";
                        while (i < s.length() && Character.isDigit(s.charAt(i)))
                            operand += s.charAt(i++);
                        --i;
                        st.add(Double.parseDouble(operand)); // для чисел, которые содержат больше одной цифры
                }
            }
            while (!op.isEmpty())
                processOperator(st, op.removeLast(), results);
            System.out.println("Результат вычисления: " + st.get(0)); // возврат результата
            System.out.println();
            for (int i=0;i<10;i++)
            {
                if (results[i] == 0)
                {
                    results[i] = st.get(0);
                    break;
                }
            }
            return st.get(0);  // возврат результата
        }
        catch(Exception ex)
        {
            System.out.println("Проверьте корректность ввода формулы");
            return null;
        }
    }

    public static void main  (String[] args) throws Exception
    {
        Main obj = new Main();
        double[] results = new double[100]; // резервирование результатов 100 выражений
        int number = 1;
        Scanner scan = new Scanner(System.in);
        System.out.println("Для использования результата предыдущих выражений используйте: '#номер_выражения'");
        System.out.println("Для использования тригонометрических функций используйте следующие сокращения:");
        System.out.println("s(x) = sin(x)");
        System.out.println("c(x) = cos(x)");
        System.out.println("t(x) = tg(x)");
        while (true) {
            System.out.println("Введите выражение #" + number + ":");
            String formula = scan.nextLine();
            obj.eval(formula, results);
            number++;
        }
    }
}
