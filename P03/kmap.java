import java.util.Scanner;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.TimeUnit;
import java.lang.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

class KarnaughMap extends JFrame
{
    private String function;
    private String noCare;
    private String finalFunc;
    private String terms[];
    private int variables;
    private int noOfTerms;
    private int map[][];
    private int included[][];

    KarnaughMap(int var)
    {
        variables = var;
        map = new int[2 * (variables / 2)][2 * (variables - variables / 2)];
        included = new int[2 * (variables / 2)][2 * (variables - variables / 2)];
    }

    public void termCount(int termNumber)
    {
        noOfTerms = termNumber;
        terms = new String[100];
    }

    public void paint(Graphics g)
    {
        int i, j;
        TextField tf = new TextField(20);
        int rows, columns;
        rows = 2 * (variables / 2);
        columns = 2 * (variables - variables / 2);

        int width, height;
        width = 100 * (2 * (variables - variables / 2));
        height = 100 * (2 * (variables / 2));

        g.setFont(new Font("Calibri", Font.PLAIN, 24));

        g.clearRect(0, 0, 1920, 1080);
        g.drawRect(550, 150, width, height);
        g.drawString("Karnaugh Map", 625, 75);

        int y = 150;
        while (y < 150 + height)
        {
            g.drawLine(550, y, 550 + width, y);
            y = y + 100;
        }

        int x = 550;
        while (x < 550 + width)
        {
            g.drawLine(x, 150, x, 150 + height);
            x = x + 100;
        }

        for (i = 0; i < rows; i++)
        {
            int a[] = new int[1];
            storeVal(a, 0, i);

            String s = convert(a, 0);

            g.drawString(s, 505, 200 + i * 100);
        }

        for (j = 0; j < columns; j++)
        {
            int a[] = new int[1];
            storeVal(a, 0, j);

            String s = convert(a, variables / 2);

            g.drawString(s, 590 + 100 * j, 135);
        }

        String s;
        for (i = 550; i < 550 + width; i = i + 100)
        {
            for (j = 150; j < 150 + height; j = j + 100)
            {
                if (map[(j - 150) / 100][(i - 550) / 100] == 1)
                {
                    s = "1";
                }
                else if (map[(j - 150) / 100][(i - 550) / 100] == -1)
                {
                    s = "X";
                }
                else
                {
                    s = "0";
                }

                g.drawString(s, i + 45, j + 55);
            }
        }

        if (finalFunc.charAt(0) != '1')
        {
            finalFunc = finalFunc.substring(1, finalFunc.length());
        }
        g.drawString("Minimized function :- " + finalFunc, 550, j + 50);
    }

    public void display()
    {
        setSize(1920, 1080);
        setLocation(100, 100);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        repaint();
    }

    public boolean condition(String s, int charIndex)
    {
        if ((s.length() == charIndex - 1 && variables > charIndex - 1) || (s.length() > charIndex - 1 && s.charAt(charIndex - 1) != 64 + charIndex && s.charAt(charIndex - 1) != 96 + charIndex))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void update(String s, int charIndex, int newTerm, int term)
    {
        terms[term] = s.substring(0, charIndex - 1) + (char)(96 + charIndex) + s.substring(charIndex - 1, s.length());
        terms[noOfTerms + newTerm] = s.substring(0, charIndex - 1) + (char)(64 + charIndex) + s.substring(charIndex - 1, s.length());
    }

    public void split(String func, boolean care)
    {
        int term;
        term = 0;
        terms[term] = "";

        for (int i = 0; i < func.length(); i++)
        {
            char ch = func.charAt(i);

            if (ch == '+')
            {
                term += 1;
                terms[term] = "";
            }
            else if (ch != ' ')
            {
                terms[term] += ch;
            }
        }
        terms[term + 1] = null;

        int newTerm = 0;
        String s;
        term = 0;
        while (terms[term] != null)
        {
            s = terms[term];
            if (condition(s, 1) == true)
            {
                terms[term] = 'a' + s;
                terms[noOfTerms + newTerm] = 'A' + s;
                s = terms[term];
                newTerm += 1;
            }
            if (condition(s, 2) == true)
            {
                update(s, 2, newTerm, term);
                s = terms[term];
                newTerm += 1;
            }
            if (condition(s, 3) == true)
            {
                update(s, 3, newTerm, term);
                s = terms[term];
                newTerm += 1;
            }
            if (condition(s, 4) == true)
            {
                update(s, 4, newTerm, term);
                s = terms[term];
                newTerm += 1;
            }
            term += 1;
        }
    }

    public int findValue(int arr[])
    {
        int x = 1;
        int value = 0;

        for (int i = arr.length - 1; i >= 0; i--)
        {
            value += x * arr[i];
            x = x * 2;
        }

        if (value == 3)
        {
            value = 2;
        }
        else if (value == 2)
        {
            value = 3;
        }
        return value;
    }

    public void fillMap(boolean care)
    {
        int term = 0;
        while(terms[term] != null)
        {
            int elementsPresent[] = new int[variables];
            for (int i = 0; i < terms[term].length(); i++)
            {
                char ch = terms[term].charAt(i);
                if (ch >= 65 && ch <= 68)
                {
                    elementsPresent[ch - 65] = 1;
                }
            }

            int vertical[] = new int[variables / 2];
            for (int i = 0; i < variables / 2; i++)
            {
                vertical[i] = elementsPresent[i];
            }
            int row = findValue(vertical);

            int horizontal[] = new int[variables - variables / 2];
            for (int i = 0; i < variables - variables / 2; i++)
            {
                horizontal[i] = elementsPresent[i + variables / 2];
            }
            int column = findValue(horizontal);

            map[row][column] = 1;
            if (care == false)
            {
                map[row][column] = -1;
                included[row][column] = 1;
            }
            term += 1;
        }

        System.out.println("\nK-map :- \n");
        for (int i = 0; i < 2 * (variables / 2); i++)
        {
            for (int j = 0; j < 2 * (variables - variables / 2); j++)
            {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    public String convert(int arr[], int k)
    {
        String s = "";
        int X;
        if (k != 0)
        {
            X = variables - variables / 2;
        }
        else
        {
            X = variables / 2;
        }

        int matrix[][] = new int[arr.length][X];
        for (int i = 0; i < arr.length; i++)
        {
            for (int j = X - 1; j >= 0; j--)
            {
                if (arr[i] % 2 == 1)
                {
                    matrix[i][j] = 1;
                }
                arr[i] = arr[i] / 2;
            }
        }

        boolean flag;
        for (int j = 0; j < X; j++)
        {
            int x = matrix[0][j];
            flag = true;
            for (int i = 0; i < arr.length; i++)
            {
                if (matrix[i][j] != x)
                {
                    flag = false;
                    break;
                }
            }

            if (flag == true && x == 0)
            {
                s += (char)(97 + k + j);
            }
            else if (flag == true && x == 1)
            {
                s += (char)(65 + k + j);
            }
        }
        return s;
    }

    public void storeVal(int arr[], int index, int val)
    {
        if (val == 2)
        {
            arr[index] = 3;
        }
        else if (val == 3)
        {
            arr[index] = 2;
        }
        else
        {
            arr[index] = val;
        }
    }

    public void solve()
    {
        int rows, columns;
        rows = 2 * (variables / 2);
        columns = 2 * (variables - variables / 2);

        boolean flag = true;

        finalFunc = "";

        for (int x = 1; x <= 4; x = x * 2)
        {
            for (int k = 0; k <= 1 + rows - rows / x; k++)
            {

                flag = true;
                int count = 0;

                OUTER_LOOP:
                for (int i = k; i < k + rows / x; i++)
                {
                    for (int j = 0; j < columns; j++)
                    {
                        if (included[i % rows][j] == 1)
                        {
                            count += 1;
                        }
                        if (map[i % rows][j] == 0)
                        {
                            flag = false;
                            break OUTER_LOOP;
                        }
                    }
                }

                if (x == 1 && flag == true)
                {
                    finalFunc += "1";
                    System.out.println(finalFunc);
                    return;
                }

                if (flag == true && count != (columns * (rows / x)))
                {
                    int row_val[] = new int[rows / x];
                    for (int i = k; i < k + rows / x; i++)
                    {
                        int y = i % rows;
                        storeVal(row_val, i - k, y);
                    }
                    finalFunc += " + " + convert(row_val, 0);

                    for (int i = k; i < k + rows / x; i++)
                    {
                        for (int j = 0; j < columns; j++)
                        {
                            included[i % rows][j] = 1;
                        }
                    }
                }
            }

            for (int k = 0; k <= 1 + columns - columns / x; k++)
            {
                flag = true;

                int count = 0;

                OUTER_LOOP:
                for (int j = k; j < k + columns / x; j++)
                {
                    for (int i = 0; i < rows; i++)
                    {
                        if (included[i][j % columns] == 1)
                        {
                            count += 1;
                        }
                        if (map[i][j % columns] == 0)
                        {
                            flag = false;
                            break OUTER_LOOP;
                        }
                    }
                }

                if (flag == true && count != ((columns / x) * rows))
                {
                    int column_val[] = new int[columns / x];
                    for (int j = k; j < k + columns / x; j++)
                    {
                        int y = j % columns;
                        storeVal(column_val, j - k, y);
                    }
                    finalFunc += " + " + convert(column_val, variables / 2);

                    for (int j = k; j < k + columns / x; j++)
                    {
                        for (int i = 0; i < rows; i++)
                        {
                            included[i][j % columns] = 1; //change to all including above
                        }
                    }
                }
            }
        }

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                if (map[i][j] != 0 && map[(i + 1) % rows][j] != 0 && map[i][(j + 1) % columns] != 0 && map[(i + 1) % rows][(j + 1) % columns] != 0)
                {
                    if (included[i][j] != 1 || included[(i + 1) % rows][j] != 1 || included[i][(j + 1) % columns] != 1 || included[(i + 1) % rows][(j + 1) % columns] != 1)
                    {
                        int rowVal[] = new int[2];
                        int columnVal[] = new int[2];
                        included[i][j] = 1;
                        included[(i + 1) % rows][j] = 1;
                        included[i][(j + 1) % columns] = 1;
                        included[(i + 1) % rows][(j + 1) % columns] = 1;

                        storeVal(rowVal, 0, i);
                        storeVal(rowVal, 1, (i + 1) % rows);

                        storeVal(columnVal, 0, j);
                        storeVal(columnVal, 1, (j + 1) % columns);

                        finalFunc += " + " + convert(rowVal, 0) + convert(columnVal, variables / 2);
                    }
                }
            }
        }

        for (int i = 0; i < rows; i++)
        {
            for (int j = 0; j < columns; j++)
            {
                if (included[i][j] != 1 && map[i][j] == 1)
                {
                    included[i][j] = 1;


                    int x = i - 1 < 0 ? rows - 1 : i - 1;
                    int y = j - 1 < 0 ? columns - 1 : j - 1;
                    if (map[(i + 1) % rows][j] == 1)
                    {
                        included[(i + 1) % rows][j] = 1;
                        int rowVal[] = new int[2];
                        int columnVal[] = new int[1];

                        storeVal(rowVal, 0, i);
                        storeVal(rowVal, 1, (i + 1) % rows);

                        storeVal(columnVal, 0, j);

                        finalFunc += " + " + convert(rowVal, 0) + convert(columnVal, variables / 2);
                    }
                    else if (map[i][(j + 1) % columns] == 1)
                    {
                        included[i][(j + 1) % columns] = 1;
                        int rowVal[] = new int[1];
                        int columnVal[] = new int[2];

                        storeVal(rowVal, 0, i);

                        storeVal(columnVal, 0, j);
                        storeVal(columnVal, 1, (j + 1) % columns);

                        finalFunc += " + " + convert(rowVal, 0) + convert(columnVal, variables / 2);
                    }
                    else if (map[x][j] == 1)
                    {
                        included[x][j] = 1;
                        int rowVal[] = new int[2];
                        int columnVal[] = new int[1];

                        storeVal(rowVal, 0, i);
                        storeVal(rowVal, 1, x);

                        storeVal(columnVal, 0, j);

                        finalFunc += " + " + convert(rowVal, 0) + convert(columnVal, variables / 2);
                    }
                    else if (map[i][y] == 1)
                    {
                        // does statement between else if work
                        included[i][y] = 1;
                        int rowVal[] = new int[1];
                        int columnVal[] = new int[2];

                        storeVal(rowVal, 0, i);

                        storeVal(columnVal, 0, j);
                        storeVal(columnVal, 1, y);

                        finalFunc += " + " + convert(rowVal, 0) + convert(columnVal, variables / 2);
                    }
                    else if (map[(i + 1) % rows][j] == -1)
                    {
                        included[(i + 1) % rows][j] = 1;
                        int rowVal[] = new int[2];
                        int columnVal[] = new int[1];

                        storeVal(rowVal, 0, i);
                        storeVal(rowVal, 1, (i + 1) % rows);

                        storeVal(columnVal, 0, j);

                        finalFunc += " + " + convert(rowVal, 0) + convert(columnVal, variables / 2);
                    }
                    else if (map[i][(j + 1) % columns] == -1)
                    {
                        included[i][(j + 1) % columns] = 1;
                        int rowVal[] = new int[1];
                        int columnVal[] = new int[2];

                        storeVal(rowVal, 0, i);

                        storeVal(columnVal, 0, j);
                        storeVal(columnVal, 1, (j + 1) % columns);

                        finalFunc += " + " + convert(rowVal, 0) + convert(columnVal, variables / 2);
                    }
                    else if (map[x][j] == -1)
                    {
                        included[x][j] = 1;
                        int rowVal[] = new int[2];
                        int columnVal[] = new int[1];

                        storeVal(rowVal, 0, i);
                        storeVal(rowVal, 1, x);

                        storeVal(columnVal, 0, j);

                        finalFunc += " + " + convert(rowVal, 0) + convert(columnVal, variables / 2);
                    }
                    else if (map[i][y] == -1)
                    {
                        // does statement between else if work
                        included[i][y] = 1;
                        int rowVal[] = new int[1];
                        int columnVal[] = new int[2];

                        storeVal(rowVal, 0, i);

                        storeVal(columnVal, 0, j);
                        storeVal(columnVal, 1, y);

                        finalFunc += " + " + convert(rowVal, 0) + convert(columnVal, variables / 2);
                    }

                    else
                    {
                        int rowVal[] = new int[1];
                        int columnVal[] = new int[1];

                        storeVal(rowVal, 0, i);

                        storeVal(columnVal, 0, j);

                        finalFunc += " + " + convert(rowVal, 0) + convert(columnVal, variables / 2);
                    }
                }
            }
        }

        System.out.print("\nMinimized function :- ");
        System.out.println(finalFunc.substring(3, finalFunc.length()));

    }
}

class kmap
{
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);

        String numberOfVariables = JOptionPane.showInputDialog(null, "Enter the number of variables :- ");
        int numberOfVar = numberOfVariables.charAt(0) - 48;

        String numberOfTerms = JOptionPane.showInputDialog(null, "Enter the number of terms :- ");

        int t;
        if (numberOfTerms.length() > 1)
        {
            t = (numberOfTerms.charAt(1) - 48) + 10 * (numberOfTerms.charAt(0) - 48);
        }
        else
        {
            t = (numberOfTerms.charAt(0) - 48);
        }

        String function = JOptionPane.showInputDialog(null, "Enter the function to be minimized :- ");

        KarnaughMap k = new KarnaughMap(numberOfVar);

        k.termCount(t);
        k.split(function, true);
        k.fillMap(true);

        String numberOfTerms2 = JOptionPane.showInputDialog(null, "Enter the number of terms :- ");

        int t2;
        if (numberOfTerms2.length() > 1)
        {
            t2 = (numberOfTerms2.charAt(1) - 48) + 10 * (numberOfTerms2.charAt(0) - 48);
        }
        else
        {
            t2 = (numberOfTerms2.charAt(0) - 48);
        }

        String noCare = JOptionPane.showInputDialog(null, "Enter the don't care condition :- ");

        k.termCount(t2);
        k.split(noCare, false);
        k.fillMap(false);

        k.solve();
        k.display();
    }
}
