package br.com.frametcc.database.api;

import java.io.File;
import java.util.List;

public abstract class DBConfigFile {

    private int databaseVersion;
    private String databaseName;
    private File createScriptsAssetsFolder;
    private File updateScriptsAssetsFolder;
    private boolean preLoadMetadados;

    public DBConfigFile(String databaseName, File updateScriptsAssetsFolder, File createScriptsAssetsFolder) {
        this(1, databaseName, updateScriptsAssetsFolder, createScriptsAssetsFolder, false);
    }

    public DBConfigFile(int databaseVersion, String databaseName, File updateScriptsAssetsFolder, File createScriptsAssetsFolder) {
        this(databaseVersion, databaseName, updateScriptsAssetsFolder, createScriptsAssetsFolder, false);
    }

    public DBConfigFile(int databaseVersion, String databaseName, File updateScriptsAssetsFolder, File createScriptsAssetsFolder, boolean preLoadMetadados) {
        this.databaseVersion = databaseVersion;
        this.databaseName = databaseName;
        this.preLoadMetadados = preLoadMetadados;
        this.updateScriptsAssetsFolder = updateScriptsAssetsFolder;
        this.createScriptsAssetsFolder = createScriptsAssetsFolder;
    }

    /**
     * Versão do banco de dados.
     */
    public int getDatabaseVersion() {
        return this.databaseVersion;
    }

    /**
     * Nome do banco de dados
     */
    public String getDataBaseName() {
        return this.databaseName;
    }

    /**
     * Local onde ficam os scripts de atualização do banco de dados
     */
    public File getUpdateScriptsAssetsFolder() {
        return updateScriptsAssetsFolder;
    }

    /**
     * Local onde ficam os scripts de criação do banco de dados
     */
    public File getCreateScriptsAssetsFolder() {
        return createScriptsAssetsFolder;
    }

    /**
     * Classes que representem as tabelas do banco, devidamente anotadas com @Table e @Column
     */
    public abstract List<Class> getTableClasses();

}