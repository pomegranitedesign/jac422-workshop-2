package com.company;
import javax.management.StandardMBean;
import java.util.Scanner;

public class Tax {
  public static final int SINGLE_FILER = 0;
  public static final int MARRIED_JOINTLY_OR_QUALIFYING_WIDOW = 1;
  public static final int MARRIED_SEPARATELY = 2;
  public static final int HEAD_OF_HOUSEHOLD = 3;

  private int filingStatus;
  private int[][] brackets; // Stores the tax brackets for each filling status
  private double[] rates; // Stores the tax rates for each bracket
  private double taxableIncome;

  public int getFillingStatus() { return filingStatus; }
  public double getTaxIncome() { return taxableIncome; }

  public void setFilingStatus(int filingStatus) { this.filingStatus = filingStatus; }
  public void setTaxableIncome(double taxableIncome) { this.taxableIncome = taxableIncome; }

  public Tax() {
    filingStatus = 0;
    taxableIncome = 0;
  }

  public Tax(int filingStatus, int[][] brackets, double[] rates, double taxableIncome) {
    this.filingStatus = filingStatus;
    this.brackets = brackets;
    this.rates = rates;
    this.taxableIncome = taxableIncome;
  }

  public void calculate() {
    double taxRate = 0.0;
    boolean isAppRunning = true;
    while (isAppRunning) {
      int choice = displayMenu();

      if (choice == 1) {
        enterFilingStatus();
        enterTaxableIncome();
        taxRate = getTaxRate(filingStatus, taxableIncome);
        System.out.println("\nTaxable income: " + (taxableIncome * taxRate) + '\n');
      } else if (choice == 2) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the amount from: $");
        double from = in.nextDouble();

        in.nextLine();

        System.out.print("Enter the amount to: $");
        double to = in.nextDouble();

        // TODO: Refactor code
        System.out.println("2001 tax tables for taxable income from $" + from + "to $" + to);
        System.out.println("------------------------------------------------------------------------------------------");
        System.out.println("Taxable Income   Single      Married Joint or Qualifying Widow   Married   Head of a House");
        System.out.println("------------------------------------------------------------------------------------------");

        for (double i = from; i <= to; i += 1000) {
          System.out.format(
            "%-15d  %8.2f  %10.2f  %34.2f  %4.2f",
            (int)i,
            i * getTaxRate(SINGLE_FILER, i),
            i * getTaxRate(MARRIED_JOINTLY_OR_QUALIFYING_WIDOW, i),
            i * getTaxRate(MARRIED_SEPARATELY, i),
            i * getTaxRate(HEAD_OF_HOUSEHOLD, i)
          );
          System.out.println();
        }
        System.out.println();
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

  private double getTaxRate(int filingStatus, double taxableIncome) {
    switch (filingStatus) {
      case SINGLE_FILER:
        if (taxableIncome <= 27050) return 0.15;
        else if (isBetween(taxableIncome, 27051, 65550)) return 0.275;
        else if (isBetween(taxableIncome, 65551, 136750)) return 0.305;
        else if (isBetween(taxableIncome, 136751, 297350)) return 0.355;
        else if (taxableIncome >= 297351) return 0.391;

      case MARRIED_SEPARATELY:
        if (taxableIncome <= 45200) return 0.15;
        else if (isBetween(taxableIncome, 45201, 109250)) return 0.275;
        else if (isBetween(taxableIncome, 109251, 166500)) return 0.305;
        else if (isBetween(taxableIncome, 166501, 297350)) return 0.355;
        else if (taxableIncome >= 297351) return 0.391;

      case MARRIED_JOINTLY_OR_QUALIFYING_WIDOW:
        if (taxableIncome <= 22600) return 0.15;
        else if (isBetween(taxableIncome, 22601, 54625)) return 0.275;
        else if (isBetween(taxableIncome, 54626, 83250)) return 0.305;
        else if (isBetween(taxableIncome, 83251, 148675)) return 0.355;
        else if (taxableIncome >= 148676) return 0.391;

      case HEAD_OF_HOUSEHOLD:
        if (taxableIncome <= 36250) return 0.15;
        else if (isBetween(taxableIncome, 65551, 136750)) return 0.305;
        else if (isBetween(taxableIncome, 136751, 297350)) return 0.355;
        else if (taxableIncome >= 297351) return 0.391;
    }
    return 0.0;
  }

  private boolean isBetween(double value, double a, double b) { return value >= a && value <= b; }
}
