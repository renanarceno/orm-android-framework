package br.com.frametcc.shared.api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import br.com.frametcc.TCCApplication;

/**
 * Contrato para implementações da camada do presenter. Contém métodos para o correto funcionamento do framework
 *
 * @param <VIEW>  Visão relacionada a esse presenter
 * @param <MODEL> Modelo relacionado a esse presenter
 */
public interface BasePresenter<VIEW extends BaseView<?>, MODEL extends BaseModel<?>> {

	/**
	 * Chamado ao criar sua implementação
	 */
	void init();

	/**
	 * Quando a visão relacionada a esse presenter é destruída, quando "onDestroy" é chamado na Activity, sinaliza para o presenter que sua visão não existe mais
	 *
	 * @see android.app.Activity
	 */
	void destroy();

	/**
	 * Seta o modelo correspondente no presenter
	 *
	 * @param model o modelo para ser setado
	 */
	void setModel(MODEL model);

	/**
	 * Seta a view correspondente no presenter
	 *
	 * @param view o view para ser setado
	 */
	void setView(VIEW view);

	/**
	 * Caso um presenter queira se comunicar com outro, esse método retorna a implementação atual do presenter passado em 'control'
	 *
	 * @param control o presenter que se quer uma implementação
	 * @return a implementação atual do presenter passado em 'control'
	 */
	<C extends BasePresenter<?, ?>, CI extends C> CI getPresenter(Class<CI> control);

	/**
	 * @return instancia de TCCApplication, que sempre é única.
	 */
	TCCApplication getApplication();

	/**
	 * Quando uma activity é criada, chamado o método 'onCreate', avisa o presenter correspondente.
	 *
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	void onCreateActivity(Bundle savedInstanceState);

	/**
	 * Quando uma activity é startada, chamado o método 'onStart', notifica o presenter correspondente.
	 *
	 * @see Activity#onStart()
	 */
	void onStartActivity();

	/**
	 * Quando uma activity é resumida, chamado o método 'onResuma', o presenter correspondente é notificado.
	 *
	 * @see Activity#onResume()
	 */
	void onResumeActivity();

	/**
	 * Quando uma activity é pausada, chamado o método 'onPause', o presenter correspondente é notificado.
	 *
	 * @see Activity#onPause()
	 */
	void onPauseActivity();

	/**
	 * Quando uma activity é parada, chamado o método 'onStop', o presenter correspondente é notificado.
	 *
	 * @see Activity#onStop()
	 */
	void onStopActivity();

	/**
	 * Quando uma activity é reiniciada, chamado o método 'onRestart', o presenter correspondente é notificado.
	 *
	 * @see Activity#onDestroy()
	 */
	void onRestartActivity();

	/**
	 * Quando uma activity é destruída, chamado o método 'onDestroy', o presenter correspondente é notificado.
	 *
	 * @see Activity#onDestroy()
	 */
	void onDestroyActivity();

	/**
	 * Quando o botão de voltar é pressionado na activity, o presenter é avisado
	 *
	 * @see Activity#onBackPressed()
	 */
	void onBackPressedActivity();

	/**
	 * Quando é feito uma requisição para outra activity e uma resposta é esperada. Esse método é chamado para receber a resposta
	 *
	 * @see android.app.Activity#onActivityResult(int, int, Intent)
	 */
	void onActivityResult(int requestCode, int resultCode, Intent data);

	/**
	 * Quando o aplicativo vai para segundo plano, a aplicação entra em um estado de "pausa". Quando o aplicativo
	 * volta a ser visível para o usuario, esse método é chamado notificando o presenter
	 */
	void onApplicationRestarted();

	/**
	 * NOvo esquema de requisição de permissões no Android. Chamado ao receber a resposta de uma requisição de permissão.
	 *
	 * @see android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback#onRequestPermissionsResult(int, String[], int[])
	 */
	void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults);
}
