package br.com.frametcc;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Map;

import br.com.frametcc.database.DAOHelper;
import br.com.frametcc.database.DBConnectionHelper;
import br.com.frametcc.database.api.TableSpec;
import br.com.frametcc.database.dao.DatabaseDAO;
import br.com.frametcc.factory.DAOFactory;
import br.com.frametcc.factory.ModelViewPresenterFactory;
import br.com.frametcc.shared.api.BaseModel;
import br.com.frametcc.shared.api.BasePresenter;
import br.com.frametcc.shared.api.BaseView;

/**
 * Ponto de entrada para os aplicativos android. É aqui que o framework se "conecta" à aplicação desenvolvida.
 */
public abstract class TCCApplication extends Application {
	private static final int DEFAULT_DB_VERSION = 0;
	public static final String DB_VERSION = "tccframework.dbVersion";
	public static final String DB_NAME = "tccframework.dbName";
	public static final String DB_ASSETS_DB_CREATE_FOLDER = "tccframework.dbAssets.dbCreateFolder";
	public static final String DB_ASSETS_DB_UPDATE_FOLDER = "tccframework.dbAssets.dbUpdateFolder";

	private static DBConnectionHelper dbConnection;

	private final TCCLog log = TCCLog.getInstance();
	private Activity lastActivity;
	private ModelViewPresenterFactory factory;

	@Override
	@SuppressWarnings("all")
	public final void onCreate() {
		super.onCreate();
		configDatabase();
		this.create();

		this.factory = new ModelViewPresenterFactory();
		final DAOFactory daoFactory = new DAOFactory(this.getApplicationContext());
		daoFactory.daos = getDaosIntImpl();
		if (dbConnection != null)
			dbConnection.setDaoFactory(daoFactory);
		this.factory.viewPresenterImpl = getControlViewMap();
		this.factory.modelPresenterImpl = getPresenterModelMap();
		this.onAfterCreate();
	}

	@SuppressWarnings("all")
	private void configDatabase() {
		ApplicationInfo appInfo = null;
		try {
			appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
		} catch (PackageManager.NameNotFoundException ignore) {
		}

		Bundle metaData = appInfo.metaData;
		if (metaData == null)
			metaData = new Bundle();
		int dbVersion = metaData.getInt(DB_VERSION, DEFAULT_DB_VERSION);
		if (dbVersion == DEFAULT_DB_VERSION)
			log.w("Database VERSION not set");
		String dbName = metaData.getString(DB_NAME);
		if (dbName == null || dbName.trim().isEmpty())
			log.w("Database NAME not set");

		String dbCreateFolder = metaData.getString(DB_ASSETS_DB_CREATE_FOLDER);
		String dbUpdateFolder = metaData.getString(DB_ASSETS_DB_UPDATE_FOLDER);

		if (dbVersion != DEFAULT_DB_VERSION && !dbName.isEmpty())
			dbConnection = new DBConnectionHelper(this.getApplicationContext(), dbName, null, dbVersion, dbCreateFolder, dbUpdateFolder);
	}

	/**
	 * @param entityType Classe da entidade necessária para
	 * @return uma instancia de TableSpec que contém as informações de entity
	 * @see TableSpec
	 */
	@SuppressWarnings("all")
	public static <E> TableSpec<E> getTableSpec(Class<E> entityType) {
		if (dbConnection != null) return dbConnection.getTableSpec(entityType);
		return null;
	}

	/**
	 * DBConnectionHelper é a classe responsável por fazer a comunicação com o banco
	 *
	 * @return
	 * @see br.com.frametcc.database.api.DBConnection
	 */
	public static DBConnectionHelper getDbConnection() {
		if (dbConnection != null) return dbConnection;
		return null;
	}

	/**
	 * Cria o presenter e o model associado a essa view
	 *
	 * @param view classe que estende BaseView para atribuir as instancias corretas de Model e Presenter
	 * @see BaseView
	 */
	public void setupView(BaseView view) {
		this.factory.createPresenterInstance(view);
	}

	/**
	 * Informa qual a última activity visualizada pelo usuário
	 *
	 * @param activity última activity
	 */
	public void setTopActivity(Activity activity) {
		this.lastActivity = activity;
	}

	/**
	 * Sinaliza quando o aplicativo ficou em segundo plano (totalmente invisível ao usuário).
	 */
	public void onActivityStopped(Activity activity) {
		if (this.lastActivity.equals(activity))
			log.w("App stopped");
	}

	/**
	 * Sinaliza quando o aplicativo voltou a ser visível e o usuário consegue interagir com ele novamente.
	 */
	public void onApplicationRestarted() {
		log.w("App restarted");
	}

	/**
	 * Chamado quando uma activity é destruída
	 */
	public void onActivityDestroyed(Activity activity) {
		BaseView v = ((BaseView) activity);
		this.factory.removePresenter(v);
		log.w("Activity " + activity.getClass().getSimpleName() + " destroyed");
	}


	/**
	 * @param clazz interface da classe que extende de DatabaseDAO que contém as queries para o banco
	 * @return uma instancia de conexão com o banco, utilizando o padrão DAO
	 */
	public <T extends DatabaseDAO> T getDao(Class<T> clazz) {
		if (dbConnection != null) return dbConnection.getDao(clazz);
		return null;
	}

	/**
	 * @param control Classe que extende de BasePresenter
	 * @return a instancia de um presenter dado uma interface 'control'
	 */
	@Nullable
	public <C extends BasePresenter<?, ?>, CI extends C> CI getControl(Class<CI> control) {
		return this.factory.getPresenter(control);
	}

	/**
	 * @param viewInterface Classe que extende de BaseView
	 * @return retorna uma implementação de visão a partir da interface 'viewINterface'
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public <V extends BaseView<C>, C extends BasePresenter<V, ?>> Class<V> getViewImpl(Class<V> viewInterface) {
		return (Class<V>) this.factory.getViewImpl(viewInterface);
	}

	/**
	 * Chamado ao criar a aplicação
	 */
	public void create() {
	}

	/**
	 * Chamado assim que a aplicação inicializa as Factories
	 */
	public void onAfterCreate() {
	}

	/**
	 * Mapeamento da implementação do controle com sua implementação da visão
	 */
	public abstract Map<Class<? extends BaseView>, Class<? extends BasePresenter>> getControlViewMap();

	public abstract Map<Class<? extends BasePresenter>, Class<? extends BaseModel>> getPresenterModelMap();

	/**
	 * Mapeamento da interface do DAO com sua implementação, que estende de DBHelper
	 */
	public abstract Map<Class<? extends DatabaseDAO>, DAOHelper> getDaosIntImpl();
}