package br.com.frametcc.shared.api;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import br.com.frametcc.view.utils.ActivityNavigator;

/**
 * Contrato para implementações da camada da visão. Contém métodos para seu correto funcionamento
 *
 * @param <PRESENTER> presenter relacionado a esse modelo.
 */
public interface BaseView<PRESENTER extends BasePresenter> {

	/**
	 * @param layoutInflater     responsável por inflar os layouts em XML
	 * @param savedInstanceState bundle que contém os dados salvos ao destruyir a activity e recriala
	 * @return instancia da VIew inflada por layoutInflater
	 */
	View onCreateView(LayoutInflater layoutInflater, Bundle savedInstanceState);

	/**
	 * Chamada após criar a visão que foi inflada a partir do XML
	 *
	 * @param savedInstanceState bundle que contém os dados salvos ao destruir a activity e recriala
	 */
	void onAfterCreateView(Bundle savedInstanceState);

	/**
	 * Quando o aplicativo vai para segundo plano, a aplicação entra em um estado de "pausa". Quando o aplicativo
	 * volta a ser visível para o usuario, esse método é chamado
	 */
	void onApplicationRestarted();

	/**
	 * @return Um objeto auxiliar que "pega" os widgets da tela e retorna um objeto para ser usado.
	 */
	ActivityNavigator getNavigator();

	/**
	 * @param presenter setar o presenter no objeto da view
	 */
	void setPresenter(PRESENTER presenter);

	/**
	 * @return O presenter associado a essa visão
	 */
	PRESENTER getPresenter();

	/**
	 * Quando o botão de voltar é pressionado
	 */
	void onBackPressed();

	/**
	 * Quando onDEstroy for chamado
	 */
	void destroy();

	/**
	 * método auxiliar para mostrar uma mensagem, Toast, na tela
	 *
	 * @param msg mensagem a ser exibida
	 * @see android.widget.Toast
	 */
	void showToast(String msg);

	/**
	 * @return Extras associado à essa activity
	 * @see Bundle
	 */
	Bundle getExtras();

}