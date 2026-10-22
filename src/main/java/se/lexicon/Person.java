package se.lexicon;

public class Person {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phoneNumber;
    private final String address;

    private Person(Builder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.address = builder.address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public static class Builder {
        private String firstName;
        private String lastName;
        private String email;
        private String phoneNumber;
        private String address;

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Person build() {
            if (firstName == null || firstName.isBlank()
                    || lastName == null || lastName.isBlank()
                    || email == null || email.isBlank()) {
                throw new IllegalStateException(
                        "First name, last name, and email are required."
                );
            }

            return new Person(this);
        }
    }

    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName
                + "\nEmail: " + email
                + "\nPhone: " + phoneNumber
                + "\nAddress: " + address;
    }
}
