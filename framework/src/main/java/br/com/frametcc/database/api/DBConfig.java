package br.com.frametcc.database.api;

import java.util.List;

public abstract class DBConfig {

    private int databaseVersion;
    private String databaseName;

    private boolean preLoadMetadados;

    public DBConfig(String databaseName) {
        this(1, databaseName, false);
    }

    public DBConfig(int databaseVersion, String databaseName) {
        this(databaseVersion, databaseName, false);
    }

    public DBConfig(int databaseVersion, String databaseName, boolean preLoadMetadados) {
        this.databaseVersion = databaseVersion;
        this.databaseName = databaseName;
        this.preLoadMetadados = preLoadMetadados;
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
     * Classes que representem as tabelas do banco, devidamente anotadas com @Table e @Column
     */
    public abstract List<Class> getTableClasses();

}