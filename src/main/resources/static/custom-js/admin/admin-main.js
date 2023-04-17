    function fillUpdateForm(id, firstName, lastName, email, role) {
        var form = document.getElementById("updateUserForm");
        form.id.value = id;
        form.firstName.value = firstName;
        form.lastName.value = lastName;
        form.email.value = email;
        form.role.value = role;
    }

    function fillDelForm(id) {
        var form = document.getElementById("deleteUserForm");
        form.id.value = id;

    }