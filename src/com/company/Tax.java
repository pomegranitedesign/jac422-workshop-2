package com.company;
import javax.management.StandardMBean;
import java.util.Scanner;

public class Tax {
  public static final int SINGLE_FILER = 0;
  public static final int MARRIED_JOINTLY_OR_QUALIFYING_WIDOW = 1;
  public static final int MARRIED_SEPARATELY = 2;
  public static final int HEAD_OF_HOUSEHOLD = 3;

  private int filingStatus;
  private double[] rates = {}; // Stores the tax rates for each bracket
  private double taxableIncome;

  public void setFilingStatus(int filingStatus) { this.filingStatus = filingStatus; }
  public void setTaxableIncome(double taxableIncome) { this.taxableIncome = taxableIncome; }

  public Tax() {
    filingStatus = 0;
    taxableIncome = 0;
  }

  public Tax(int filingStatus, double[] rates, double taxableIncome) {
    this.filingStatus = filingStatus;
    this.rates = rates;
    this.taxableIncome = taxableIncome;
  }

  public void calculate() {
    boolean isAppRunning = true;
    while (isAppRunning) {
      int choice = displayMenu();

      if (choice == 1) {
        enterFilingStatus();
        enterTaxableIncome();
         System.out.println("\nTaxable income: " + calculateTax(taxableIncome, filingStatus, 2009) + '\n');
      } else if (choice == 2) {
        print2001TaxRates();
        print2009TaxRates();
      } else if (choice == 0) {
        System.out.println("\nThank You for using the application. Bye!");
        isAppRunning = false;
      }
    }
  }

  public int displayMenu() {
    System.out.println("Compute the personal income tax <1>");
    System.out.println("Print the tax tables for taxable incomes (with range) <2>");
    System.out.println("Exit <0>");

    Scanner in = new Scanner(System.in);
    return in.nextInt();
  }

  public void enterFilingStatus() {
    boolean isStatusEntered = false;
    int choice = 0;
    while (!isStatusEntered) {
      System.out.println("Please enter a filing status");
      System.out.println("0 -- Single Filer");
      System.out.println("1 -- Married Jointly or Qualifying Widow(er)");
      System.out.println("2 -- Married Separately");
      System.out.println("3 -- Head of Household");

      Scanner in = new Scanner(System.in);
      choice = in.nextInt();

      if (choice >= SINGLE_FILER && choice <= HEAD_OF_HOUSEHOLD) isStatusEntered = true;
      else System.out.println("Please enter a correct value <0, 1, 2, 3>");
    }
    setFilingStatus(choice);
  }

  public void enterTaxableIncome() {
    boolean isTaxableIncomeEntered = false;

    double taxableIncome = 0;
    while (!isTaxableIncomeEntered) {
      System.out.print("Please enter taxable income: $");
      Scanner in = new Scanner(System.in);
      taxableIncome = in.nextDouble();

      if (taxableIncome > 0) isTaxableIncomeEntered = true;
      else System.out.println("Income can't be a negative value");
    }
    setTaxableIncome(taxableIncome);
  }

  private double calculateTax(double taxableIncome, int filingStatus, int year) {
    double finalTax = 0;
    rates = year == 2001
      ? new double[] { 0.15, 0.275, 0.305, 0.355, 0.391 }
      : new double[] { 0.10, 0.15, 0.25, 0.28, 0.33, 0.35 };

    switch (filingStatus) {
      case SINGLE_FILER:
        int[] brackets = year == 2001
          ? new int[] { 27050, 65550, 136750, 297350, 397351 }
          : new int[] { 8350, 33950, 82250, 171550, 372550, 372951 };

        int i;
        for (i = 0; i < brackets.length; i++) {
          if (brackets[i] >= taxableIncome) break;
          int value = i > 0 ? brackets[i] - brackets[i-1] : brackets[i];
          finalTax += value * rates[i];
        }
        taxableIncome -= brackets[i-1];
        finalTax += taxableIncome * rates[i];
        break;

      case MARRIED_JOINTLY_OR_QUALIFYING_WIDOW:
        brackets = year == 2001
          ? new int[] { 45200, 109250, 166500, 297350, 197351 }
          : new int[] { 8350, 33950, 82250, 171550, 372550, 372951 };

        finalTax = 0;
        for (i = 0; i < brackets.length; i++) {
          if (brackets[i] >= taxableIncome) break;
          int value = i > 0 ? brackets[i] - brackets[i-1] : brackets[i];
          finalTax += value * rates[i];
        }
        taxableIncome -= brackets[i-1];
        finalTax += taxableIncome * rates[i];
        break;

      case MARRIED_SEPARATELY:
        brackets = year == 2001
          ? new int[] { 22600, 54625, 83250, 148675, 148676 }
          : new int[] { 8350, 33950, 82250, 171550, 372550, 372951 };

        finalTax = 0;
        for (i = 0; i < brackets.length; i++) {
          if (brackets[i] >= taxableIncome) break;
          int value = i > 0 ? brackets[i] - brackets[i-1] : brackets[i];
          finalTax += value * rates[i];
        }
        taxableIncome -= brackets[i-1];
        finalTax += taxableIncome * rates[i];
        break;

      case HEAD_OF_HOUSEHOLD:
        brackets = year == 2001
          ? new int[] { 36250, 93650, 151650, 297350, 297351 }
          : new int[] { 8350, 33950, 82250, 171550, 372550, 372951 };

        finalTax = 0;
        for (i = 0; i < brackets.length; i++) {
          if (brackets[i] >= taxableIncome) break;
          int value = i > 0 ? brackets[i] - brackets[i-1] : brackets[i];
          finalTax += value * rates[i];
        }
        taxableIncome -= brackets[i-1];
        finalTax += taxableIncome * rates[i];
        break;
    }
    return finalTax;
  }

  private void print2001TaxRates() {
    Scanner in = new Scanner(System.in);
    System.out.print("Enter the amount from: $");
    double from = in.nextDouble();

    in.nextLine();

    System.out.print("Enter the amount to: $");
    double to = in.nextDouble();

    System.out.println("2001 tax tables for taxable income from $" + from + " recrwfrrf 43rffw rerrfdferewfq`to $" + to);
    System.out.println("------------------------------------------------------------------------------------------");
    System.out.println("Taxable Income   Single      Married Joint or Qualifying Widow   Married   Head of a House");
    System.out.println("------------------------------------------------------------------------------------------");

    for (double i = from; i <= to; i += 1000) {
      System.out.format(
        "%-15d  %8.2f  %10.2f  %34.2f  %4.2f",
        (int)i,
        calculateTax(i, SINGLE_FILER, 2001),
        calculateTax(i, MARRIED_JOINTLY_OR_QUALIFYING_WIDOW, 2001),
        calculateTax(i, MARRIED_SEPARATELY, 2001),
        calculateTax(i, HEAD_OF_HOUSEHOLD, 2001)
      );
      System.out.println();
    }
    System.out.println();
  }

  private void print2009TaxRates() {
    System.out.println("2009 tax tables for taxable income from $" + from + " to $" + to);
    System.out.println("------------------------------------------------------------------------------------------");
    System.out.println("Taxable Income   Single      Married Joint or Qualifying Widow   Married   Head of a House");
    System.out.println("------------------------------------------------------------------------------------------");

    for (double i = from; i <= to; i += 1000) {
      System.out.format(
        "%-15d  %8.2f  %10.2f  %34.2f  %4.2f",
        (int)i,
        calculateTax(i, SINGLE_FILER, 2009),
        calculateTax(i, MARRIED_JOINTLY_OR_QUALIFYING_WIDOW, 2009),
        calculateTax(i, MARRIED_SEPARATELY, 2009),
        calculateTax(i, HEAD_OF_HOUSEHOLD, 2009)
      );
      System.out.println();
    }
    System.out.println();
  }
}
