package cpviajes.modelo;

public class Viajes {

    public String idViaje;
    public String nom;
    public String datain;
    public String datafi;
    public int kmin;
    public int kmfi;
    public String tipo;
    public String descrip;
    public int tgast;
    public int tkm;

    public Viajes(String idViaje, String nom, String datain, String datafi, int kmin, int kmfi,
                  String tipo, String descrip, int tgast, int tkm) {
        this.idViaje = idViaje;
        this.nom = nom;
        this.datain = datain;
        this.datafi = datafi;
        this.kmin = kmin;
        this.kmfi = kmfi;
        this.tipo = tipo;
        this.descrip = descrip;
        this.tgast = tgast;
        this.tkm = tkm;
    }
}
