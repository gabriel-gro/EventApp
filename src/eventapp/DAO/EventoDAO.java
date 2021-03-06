package eventapp.DAO;

import eventapp.excecoes.EventoExcecao;
import eventapp.excecoes.sqlExcecao;
import java.sql.PreparedStatement;  
import java.sql.ResultSet;  
import java.sql.SQLException;
import eventapp.models.Evento;
import eventapp.util.Conn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;



public class EventoDAO {
    
    public void insere(Evento evento) throws ClassNotFoundException, SQLException, sqlExcecao, IOException{
        //String nome, String descricao, String dataInicio, String dataFim, int idUsuario, String local
        String sql = "INSERT into evento (nome,descricao,data_inicio,data_fim,id_criador,local_evento) VALUES(?,?,?,?,?,?)";
        PreparedStatement ps;
        ps = Conn.conectar().prepareStatement(sql);
        ps.setString(1,evento.getNome());
        ps.setString(2,evento.getDescricao());
        ps.setDate(3,evento.getDataInicio());
        ps.setDate(4,evento.getDataFim());
        ps.setInt(5,evento.getIdUsuario());
        ps.setString(6,evento.getLocal());
        ps.executeUpdate();
        Conn.fecharConexao();
    }
    
    public ArrayList<Evento> listar() {
        try {
            String sql = "SELECT id, nome,descricao,data_inicio,data_fim,id_criador,local_evento FROM evento order by nome desc";
            PreparedStatement ps = Conn.conectar().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            Conn.fecharConexao();
            ArrayList<Evento> eventos = new ArrayList();
            while (rs.next()) {
                Evento e = new Evento(  rs.getInt("id"),
                                        rs.getString("nome"),
                                        rs.getString("descricao"),
                                        rs.getDate("data_inicio"),
                                        rs.getDate("data_fim"),
                                        rs.getInt("id_criador") ,
                                        rs.getString("local_evento"));
                eventos.add(e);
            }
            return eventos;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    public void deletar(Evento objEv) throws SQLException, EventoExcecao, ClassNotFoundException, sqlExcecao, IOException{
        this.deletar(objEv.getId());
    }
    
    public void deletar(int id) throws SQLException, ClassNotFoundException, EventoExcecao, sqlExcecao, IOException {
        String sql = "DELETE FROM evento WHERE id = " + id;
        PreparedStatement stmt = Conn.conectar().prepareStatement(sql);
        if (stmt.executeUpdate() != 0) {
            Conn.fecharConexao();
        } else {
            throw new EventoExcecao("Erro ao conectar com banco dados");
        }
    }
    
    public void atualizar(Evento evento) throws SQLException, ClassNotFoundException, sqlExcecao, EventoExcecao, IOException {
        String sql = "UPDATE evento SET nome = ?, descricao = ?, data_inicio = ?, data_fim = ?,id_criador = ?, local_evento = ? WHERE id = ?";

        PreparedStatement ps = Conn.conectar().prepareStatement(sql);
        ps.setString(1, evento.getNome());
        ps.setString(2, evento.getDescricao());
        ps.setDate(3, evento.getDataInicio());
        ps.setDate(4, evento.getDataFim());
        ps.setInt(5, evento.getIdUsuario());
        ps.setString(6, evento.getLocal());
        ps.setInt(7, evento.getId());

        ps.executeUpdate();
        Conn.fecharConexao();
    }
    
    public Evento procurarPorId(int id) {
        try {
            String sql = "SELECT id, nome,descricao,data_inicio,data_fim,id_criador,local_evento FROM evento where id = ? order by nome desc";
            PreparedStatement ps = Conn.conectar().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            Conn.fecharConexao();
            rs.next();
            Evento e = new Evento(  rs.getInt("id"),
                                    rs.getString("nome"),
                                    rs.getString("descricao"),
                                    rs.getDate("data_inicio"),
                                    rs.getDate("data_fim"),
                                    rs.getInt("id_criador"),
                                    rs.getString("local_evento"));
            return e;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
//    SELECT e.* FROM evento as e
//    where e.id not in (select id_evento from participa where id_usuario = 4);
    public ArrayList<Evento> buscarEventosQueNaoParticipo(int userId) throws SQLException, EventoExcecao, Exception {
        String sql =    "SELECT id, nome,descricao,data_inicio,data_fim,id_criador,local_evento "+
                        "FROM evento where id not in (select id_evento from participa where id_usuario = ?)";
        PreparedStatement ps = Conn.conectar().prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        Conn.fecharConexao();
        ArrayList<Evento> eventos = new ArrayList();
        while (rs.next()) {
            Evento e = new Evento(  rs.getInt("id"),
                                    rs.getString("nome"),
                                    rs.getString("descricao"),
                                    rs.getDate("data_inicio"),
                                    rs.getDate("data_fim"),
                                    rs.getInt("id_criador") ,
                                    rs.getString("local_evento"));
            eventos.add(e);
        }
        return eventos;
    }
    
    public ArrayList<Evento> buscarPorNomeNaoParticipo(int userId, String nome) throws SQLException, EventoExcecao, Exception {
        String sql = "SELECT id, nome,descricao,data_inicio,data_fim,id_criador,local_evento "+
                     "FROM evento "+
                     "where nome like'%"+nome+"%' and "+
                     "id not in (select id_evento from participa where id_usuario = ?) "+
                     "order by nome desc";
        PreparedStatement ps = Conn.conectar().prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        Conn.fecharConexao();
        ArrayList<Evento> eventos = new ArrayList();
        while (rs.next()) {
            Evento e = new Evento(  rs.getInt("id"),
                                    rs.getString("nome"),
                                    rs.getString("descricao"),
                                    rs.getDate("data_inicio"),
                                    rs.getDate("data_fim"),
                                    rs.getInt("id_criador") ,
                                    rs.getString("local_evento"));
            eventos.add(e);
        }
        return eventos;
    }
    
    public ArrayList<Evento> buscarMeusPorNome(int userId, String nome) throws SQLException, EventoExcecao, Exception {
        String sql = "SELECT id, nome,descricao,data_inicio,data_fim,id_criador,local_evento "+
                     "FROM evento "+
                     "where nome like'%"+nome+"%' and "+
                     "id_criador = ? "+
                     "order by nome desc";
        PreparedStatement ps = Conn.conectar().prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        Conn.fecharConexao();
        ArrayList<Evento> eventos = new ArrayList();
        while (rs.next()) {
            Evento e = new Evento(  rs.getInt("id"),
                                    rs.getString("nome"),
                                    rs.getString("descricao"),
                                    rs.getDate("data_inicio"),
                                    rs.getDate("data_fim"),
                                    rs.getInt("id_criador") ,
                                    rs.getString("local_evento"));
            eventos.add(e);
        }
        return eventos;

    }
    
    public ArrayList<Evento> buscarPorNome(String nome) throws SQLException, EventoExcecao, Exception {
        String sql = "SELECT id, nome,descricao,data_inicio,data_fim,id_criador,local_evento FROM evento where nome like'%"+nome+"%' order by nome desc";
        PreparedStatement ps = Conn.conectar().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Conn.fecharConexao();
        ArrayList<Evento> eventos = new ArrayList();
        while (rs.next()) {
            Evento e = new Evento(  rs.getInt("id"),
                                    rs.getString("nome"),
                                    rs.getString("descricao"),
                                    rs.getDate("data_inicio"),
                                    rs.getDate("data_fim"),
                                    rs.getInt("id_criador") ,
                                    rs.getString("local_evento"));
            eventos.add(e);
        }
        return eventos;
    }
    
    public ArrayList<Evento> buscarPorDataNaoParticipo(int userId, java.sql.Date DataAtual) throws Exception {
        String sql = "SELECT id, nome, descricao, data_inicio, data_fim, id_criador, local_evento "
                   + "FROM evento "+
                     "where id not in (select id_evento from participa where id_usuario = ?) ";
        if(DataAtual != null){
               sql += "and data_inicio <= '"+DataAtual+"' AND "
                    + "data_fim >= '"+DataAtual+"' ";
        }
        sql += " order by nome desc";
        PreparedStatement ps = Conn.conectar().prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        Conn.fecharConexao();
        ArrayList<Evento> eventos = new ArrayList();
        while (rs.next()) {
            eventos.add(new Evento( rs.getInt("id"),
                                    rs.getString("nome"),
                                    rs.getString("descricao"),
                                    rs.getDate("data_inicio"),
                                    rs.getDate("data_fim"),
                                    rs.getInt("id_criador"), 
                                    rs.getString("local_evento")));
        }
        return eventos;
    }
    
    public ArrayList<Evento> buscarMeusPorData(int userId, java.sql.Date DataAtual) throws Exception {
        String sql = "SELECT id, nome, descricao, data_inicio, data_fim, id_criador, local_evento "
                   + "FROM evento "+
                     "where id_criador = ? ";
        if(DataAtual != null){
               sql += "and data_inicio <= '"+DataAtual+"' AND "
                    + "data_fim >= '"+DataAtual+"' ";
        }
        sql += " order by nome desc";
        PreparedStatement ps = Conn.conectar().prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        Conn.fecharConexao();
        ArrayList<Evento> eventos = new ArrayList();
        while (rs.next()) {
            eventos.add(new Evento( rs.getInt("id"),
                                    rs.getString("nome"),
                                    rs.getString("descricao"),
                                    rs.getDate("data_inicio"),
                                    rs.getDate("data_fim"),
                                    rs.getInt("id_criador"), 
                                    rs.getString("local_evento")));
        }
        return eventos;
    }
    
    public ArrayList<Evento> buscarPorData(java.sql.Date DataAtual) throws Exception {
        String sql = "SELECT id, nome, descricao, data_inicio, data_fim, id_criador, local_evento "
                   + "FROM evento ";
        if(DataAtual != null){
               sql += "where data_inicio <= '"+DataAtual+"' AND "
                    + "data_fim >= '"+DataAtual+"' ";
        }
        sql += " order by nome desc";
        PreparedStatement ps = Conn.conectar().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Conn.fecharConexao();
        ArrayList<Evento> eventos = new ArrayList();
        while (rs.next()) {
            eventos.add(new Evento( rs.getInt("id"),
                                    rs.getString("nome"),
                                    rs.getString("descricao"),
                                    rs.getDate("data_inicio"),
                                    rs.getDate("data_fim"),
                                    rs.getInt("id_criador"), 
                                    rs.getString("local_evento")));
        }
        return eventos;
    }
    
}