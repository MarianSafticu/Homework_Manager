package Domain;

/**
 * the lab homework class
 * a laboratory homework has a number, description, deadline and a start date
 */
public class TemaLab implements HasID<Integer> {
    private Integer nr;
    private String descriere;
    private int deadline;
    private int startDate;

    public TemaLab(Integer nr, String descriere, int deadline, int startDate) {
        this.nr = nr;
        this.descriere = descriere;
        this.deadline = deadline;
        this.startDate = startDate;
    }

    /**
     * the getter for ID
     * @return the laboratory homework number
     */
    @Override
    public Integer getID() {
        return nr;
    }

    /**
     * the setter for ID
     * @param nr is the new laboratory homework number
     */
    @Override
    public void setID(Integer nr) {
        this.nr = nr;
    }

    /**
     * the getter for description
     * @return the description of homework
     */
    public String getDescriere() {
        return descriere;
    }

    /**
     * the setter for description
     * @param descriere is the new description of homework
     */
    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }

    /**
     * getter for the deadline
     * @return the week when is the deadline (1-14)
     */
    public int getDeadline() {
        return deadline;
    }

    /**
     * the setter for deadline
     * @param deadline is the new deadline week (1-14) for the homework
     */
    public void setDeadline(int deadline) {
        this.deadline = deadline;
    }

    /**
     * the getter for start date
     * @return the start date for homework
     */
    public int getStartDate() {
        return startDate;
    }

    /**
     * the setter for start date
     * @param startDate is the new start week (1-14) for laboratory
     */
    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    /**
     *transform a homework in a String
     * @return the string for homework
     */
    @Override
    public String toString() {
        return
                "nr=" + nr +
                ", descriere='" + descriere + '\'' +
                ", deadline=" + deadline +
                ", startDate=" + startDate ;
    }
}
