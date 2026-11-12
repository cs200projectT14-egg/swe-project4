class Member {
    String id;
    String name;
    String address;
    String status;

    Member(String id, String name, String address, String status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.status = status;
    }

    String getMemberInfo() {
        StringBuilder sb = new StringBuilder("Member info: ");

        sb.append(" ");
        sb.append(id);

        sb.append(" ");
        sb.append(name);

        sb.append(", ");
        sb.append(address);

        sb.append(" (");
        sb.append(status);
        sb.append(" )");

        return sb.toString();
    }

    void setStatus(String status) {
        this.status = status;
    }
}
