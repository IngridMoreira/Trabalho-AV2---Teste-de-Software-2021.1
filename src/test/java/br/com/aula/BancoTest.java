package br.com.aula;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import br.com.aula.exception.ContaJaExistenteException;
import br.com.aula.exception.ContaNaoExistenteException;
import br.com.aula.exception.ContaNumeroInvalidoException;
import br.com.aula.exception.ContaSemSaldoException;
import br.com.aula.exception.ValorNegativoException;

public class BancoTest {

	@Test
	public void deveCadastrarConta() throws ContaJaExistenteException, ContaNumeroInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta = new Conta(cliente, 123, 0, TipoConta.CORRENTE);
		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta);

		// Verificação
		assertEquals(1, banco.obterContas().size());
	}

	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaNumeroRepetido() throws ContaJaExistenteException, ContaNumeroInvalidoException {

		// Cenario
		Cliente cliente = new Cliente("Joao");
		Conta conta1 = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta conta2 = new Conta(cliente2, 123, 0, TipoConta.POUPANCA);

		Banco banco = new Banco();

		// Ação
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);

	}
	
	@Test(expected = ContaNumeroInvalidoException.class)
	public void naoDeveCadastrarContaComNumeroInvalido() throws ContaNumeroInvalidoException, ContaJaExistenteException{
		Cliente cliente = new Cliente("Jo�o");
		Conta conta = new Conta(cliente, 2345, 100, TipoConta.CORRENTE);
		Banco banco = new Banco();
		banco.cadastrarConta(conta);

		
	}
	
	@Test(expected = ContaJaExistenteException.class)
	public void naoDeveCadastrarContaNumeroClienteExistente() throws ContaJaExistenteException, ContaNumeroInvalidoException{
		Cliente cliente = new Cliente("Jo�o");
		Conta conta1 = new Conta(cliente, 123, 100, TipoConta.CORRENTE);
		Conta conta2 = new Conta(cliente, 345, 200, TipoConta.CORRENTE);
		Banco banco = new Banco();
		
		banco.cadastrarConta(conta1);
		banco.cadastrarConta(conta2);
	}

	@Test
	public void deveEfetuarTransferenciaContasCorrentesEPoupanca() throws ContaSemSaldoException, ContaNaoExistenteException, ValorNegativoException {


		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 0, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.POUPANCA);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));

	
		banco.efetuarTransferencia(123, 456, 100);

	
		assertEquals(-100, contaOrigem.getSaldo());
		assertEquals(100, contaDestino.getSaldo());
	}
	
	@Test(expected = ContaNaoExistenteException.class)
	public void deveVerificarAExistenciaDaContaDeOrigem() throws ContaNaoExistenteException, ContaSemSaldoException, ValorNegativoException {
		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);
		Banco banco = new Banco(Arrays.asList(contaDestino));
		
		banco.efetuarTransferencia(125, 456, 100);
		
	}
	
	@Test(expected = ContaSemSaldoException.class)
	public void naoDevePermitirQueUmaContaOrigemTipoPoupancaFiqueComSaldoNegativo() throws ContaNaoExistenteException, ContaSemSaldoException, ValorNegativoException {
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 200, TipoConta.POUPANCA);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));
	
		banco.efetuarTransferencia(123, 456, 201);
		
	}
	
	@Test(expected = ContaNaoExistenteException.class)
	public void deveVerificarAExistenciaDaContaDestinoNoBanco() throws ContaNaoExistenteException, ContaSemSaldoException, ValorNegativoException {
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 200, TipoConta.POUPANCA);
	
		Banco banco = new Banco(Arrays.asList(contaOrigem));
	
		banco.efetuarTransferencia(123, 456, 201);
		
	}
	
	@Test(expected = ValorNegativoException.class)
	public void naoDevePermitirTransferirUmValorNegativo() throws ContaNaoExistenteException, ContaSemSaldoException, ValorNegativoException {
		Cliente cliente = new Cliente("Joao");
		Conta contaOrigem = new Conta(cliente, 123, 200, TipoConta.CORRENTE);

		Cliente cliente2 = new Cliente("Maria");
		Conta contaDestino = new Conta(cliente2, 456, 0, TipoConta.CORRENTE);

		Banco banco = new Banco(Arrays.asList(contaOrigem, contaDestino));
	
		banco.efetuarTransferencia(123, 456, -100);
		
	}
	
}
