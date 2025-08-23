package unidade01;
import java.util.*;

//Daniel Santos Garcia - EX01 - 1598779

class SomarDoisNumeros {
	public static Scanner sc = new Scanner(System.in);
	
	
	public static void main(String args[]) {
		System.out.println("Digite o primeiro número: ");
		int num1 = sc.nextInt();
		System.out.println("Digite o segundo número: ");
		int num2 = sc.nextInt();
		
		int soma = num1+num2;
		
		System.out.println("O valor da soma é: " + soma);
	}
}
