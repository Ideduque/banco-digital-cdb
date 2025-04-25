package br.com.cdb.bancodigital.validation;

import jakarta.validation.constraints.NotBlank;

public class ValidadorCPF
{
    public static boolean validarCPF(String cpf)
    {
          // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^\\d]", "");

        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) return false;

        try
        {
            // Validação do primeiro dígito verificador
            int sum = 0, weight = 10;
            for (int i = 0; i < 9; i++) sum += (cpf.charAt(i) - '0') * weight--;
            int digito1 = (sum * 10) % 11;
            digito1 = (digito1 == 10) ? 0 : digito1;

            // Validação do segundo dígito verificador
            sum = 0;
            weight = 11;
            for (int i = 0; i < 10; i++) sum += (cpf.charAt(i) - '0') * weight--;
            int digito2 = (sum * 10) % 11;
            digito2 = (digito2 == 10) ? 0 : digito2;

            return digito1 == (cpf.charAt(9) - '0') && digito2 == (cpf.charAt(10) - '0');
        } catch (Exception e) {
            return false;
        }
    }
}
