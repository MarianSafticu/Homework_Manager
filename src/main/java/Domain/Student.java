package Domain;

    /**
     * store a student with an id, name, group, email and laboratory theacher
     */
    public class Student implements HasID<Integer> {
        private Integer id;
        private String nume;
        private int grupa;
        private String email;


        public Student(int id, String nume, int grupa,String email) {
            this.id = id;
            this.nume = nume;
            this.grupa = grupa;
            this.email = email;
        }

        /**
         * getter for ID
         * @return student id
         */
        @Override
        public Integer getID() {
            return id;
        }

        /**
         * setter for ID
         * @param id is the new id for the student
         */
        @Override
        public void setID(Integer id) {
            this.id = id;
        }

        /**
         * getter for name
         * @return the name of student (String)
         */
        public String getNume() {
            return nume;
        }

        /**
         * setter for name
         * @param nume is the new name for student
         */
        public void setNume(String nume) {
            this.nume = nume;
        }

        /**
         * getter for group
         * @return the group that sudent is a part of (int)
         */
        public Integer getGrupa() {
            return grupa;
        }

        /**
         * setter for group
         * @param grupa is the new group for the sudent
         */
        public void setGrupa(int grupa) {
            this.grupa = grupa;
        }



        /**
         * the method to convert to String
         * @return the student as a string
         */
        @Override
        public String toString() {
            return
                    "id=" + id +
                    ", nume = '" + nume + '\'' +
                    ", grupa = " + grupa +
                    ", email = '" + email + '\'';
        }

        /**
         * return the equality of ste student with other student ( both have the same id)
         * @param o the other student
         * @return true if both have the same id
         *         false otherwise
         */
        public boolean equals(Student o) {
            return id == o.id;
        }

        /**
         * getter for student email
         * @return the student email as String
         */
        public String getEmail() {
            return email;
        }

        /**
         * setter for email
         * @param email is the new email for student
         */
        public void setEmail(String email) {
            this.email = email;
        }
    }
