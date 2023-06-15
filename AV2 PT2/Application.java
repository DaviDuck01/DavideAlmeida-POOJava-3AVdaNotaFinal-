import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Application {
    public final Scanner ler = new Scanner(System.in);
    public final Connection conn;
    public final Statement st;
    
    public Application (Connection conn, Statement st) {
        this.conn = conn;
        this.st = st;
    }
    
    public void clear() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException e) {
            System.err.format("Erro ao limpar a tela: %s", e.getMessage());
        }
    }
    
    
    public void FrontEnd () {
        int escolha;
        while (true) {
            clear();
            System.out.println("\n---------- SEJA BEM-VINDO A LISTA DE DESEJOS ----------");
            System.out.println(" \n     OBS:Primeiro você precisa criar uma lista.\n");
            System.out.println("    1 - Criar lista nova.");
            System.out.println("    2 - Ler lista antiga.");
            System.out.println("    0 - Sair.");
            System.out.print("\nEscolha: ");
            escolha = ler.nextInt();
            clear();
            switch (escolha) {
                case 1:
                    this.createTable();
                    break;
                case 2:
                    this.insertData();
                    break;
                case 3:
                    this.readData();
                    break;
                case 4:
                    this.updateData();
                    break;
                case 5:
                    this.deleteData(";");
                    break;
                case 0:
                    System.out.println("Adeus!");
                    return;
                default:
                    System.out.println("Erro.");
                    break;
            }
        }
    }
    
    
    public void createTable () {
        try {
            System.out.println("---------- CRIAR NOVA LISTA ----------");
            System.out.print("\nNome da tabela: ");
            String nomeTabela = ler.next();
            String SQLCriarTabela = "CREATE TABLE " + nomeTabela + " (preco float, nome VARCHAR(60), tipo VARCHAR(60));";
            st.executeUpdate(SQLCriarTabela);
            System.out.print("\nCriando lista");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            clear();
            this.insertData();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }catch (InterruptedException e) {
            System.err.format("InterruptedException: %s", e.getMessage());
        }
    }
    
    public void insertData () {
        try {
            System.out.print("CPF: ");
            int CPF = ler.nextInt();
            System.out.print("Nome: ");
            String nome = ler.next();
            String SQLInserirDados = "INSERT INTO pessoa (cpf, nome) VALUES (" + CPF + ", \'" + nome + "\');";
            System.out.println(SQLInserirDados);
            st.executeUpdate(SQLInserirDados);
            System.out.println("Data inserted...");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
    
    public void readData () {
        ResultSet result;
        try {
            String SQLLerDados = "SELECT * FROM pessoa";
            result = st.executeQuery(SQLLerDados);
            while (result.next()) {
                System.out.println("--------------------------------------------------");
                System.out.println("CPF: " + result.getString(1));
                System.out.println("Nome: " + result.getString(2));
            }
            result.close();
            System.out.println("Data read...");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
    
    public void updateData () {
        try {
            System.out.print("Nome: ");
            String nome = ler.next();
            String SQLAtualizarDados = "UPDATE pessoa SET nome = \'" + nome + "\';";
            System.out.println(SQLAtualizarDados);
            st.executeUpdate(SQLAtualizarDados);
            System.out.println("Data updated...");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
    
    public void deleteData (String string) {

        try {
            System.out.println("---------- DELETAR LISTA ----------\n");

            System.out.print("listas disponíveis:\n\n");

            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, null, new String[]{"TABLE"});
            
            // Iterar sobre os resultados e imprimir os nomes das tabelas
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                System.out.println(tableName+"\n");
            }
            System.out.print("\nDigite o nome da lista que quer excluir:");
            String tabela = ler.next(); 
            String SQLdeletarDados = "DROP TABLE "+ tabela+ ";";
            st.executeUpdate(SQLdeletarDados);
            clear();
            System.out.print("\nDeletado com sucesso");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
            System.out.print(".");
            Thread.sleep(500);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }catch (InterruptedException e) {
            System.err.format("InterruptedException: %s", e.getMessage());
        }
    
    }
}
