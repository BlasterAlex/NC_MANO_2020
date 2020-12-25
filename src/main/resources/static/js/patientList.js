var app = angular.module("PatientManagement", []);

// Controller Part
app.controller("PatientController", function ($scope, $http) {

  $scope.patients = [];
  $scope.patientForm = {
    id: -1,
    surname: "",
    name: "",
    middleName: null,
    isHavingTripAbroad: false,
    contactWithPatients: false
  };

  // Load data from server
  _refreshPatientList();

  // HTTP POST/PUT methods for add/edit patient
  // Call: http://localhost:8080/ui/patient
  $scope.submitPatient = function () {

    var method = "";
    var url = "/ui/patient";

    if ($scope.patientForm.id == -1) {
      method = "POST";
      url = "/ui/patient";
    } else {
      method = "PATCH";
      url = "/ui/patient/" + $scope.patientForm.id;
    }

    $http({
      method: method,
      url: url,
      data: angular.toJson($scope.patientForm),
      headers: {
        'Content-Type': 'application/json'
      }
    }).then(_success, _error);
  };

  $scope.createPatient = function () {
    _clearFormData();
    $('#patientForm').show();
  }

  // HTTP DELETE- delete patient by Id
  // Call: http://localhost:8080/ui/patient{id}
  $scope.deletePatient = function (patient) {
    $http({
      method: 'DELETE',
      url: '/ui/patient/' + patient.id
    }).then(_success, _error);
  };

  function _success(res) {
    _refreshPatientList();
    _clearFormData();
    $('#patientForm').hide();
  }

  function _error(res) {
    var data = res.data;
    var status = res.status;
    var header = res.header;
    var config = res.config;

    // Добавление сообщения об ошибке на input
    if (typeof data === 'string' || data instanceof String) {
      var found = data.match(/\[([A-Za-z]+):\s*([^\]]+)\]/);
      if (found) {
        var field = found[1];
        var message = found[2];
        var validError = $('td input[name ="' + field + '"]').closest('tr').after('<tr><td colspan="2" class="valid-error">'
            + message + '</td></tr>').next();
        return setTimeout(() => validError.hide().remove(), 3000);
      }
    }
    alert("Error " + status + ": " + data);
    _refreshPatientList();
  }

  // HTTP GET- get patients
  // Call: http://localhost:8080/ui/patient/all
  function _refreshPatientList() {
    $http({
      method: 'GET',
      url: '/ui/patient/all'
    }).then(
        function (res) {
          $scope.patients = res.data;
        },
        function (res) {
          console.error("Error: " + res.status + " : " + res.data);
        }
    );
  }

  // Clear the form
  function _clearFormData() {
    $scope.patientForm.id = -1;
    $scope.patientForm.surname = "";
    $scope.patientForm.name = "";
    $scope.patientForm.middleName = null;
    $scope.patientForm.isHavingTripAbroad = false;
    $scope.patientForm.contactWithPatients = false;
  };

  // In case of edit
  $scope.editPatient = function (patient) {
    $scope.patientForm.id = patient.id;
    $scope.patientForm.surname = patient.surname;
    $scope.patientForm.name = patient.name;
    $scope.patientForm.middleName = patient.middleName;
    $scope.patientForm.isHavingTripAbroad = patient.isHavingTripAbroad == 'true';
    $scope.patientForm.contactWithPatients = patient.contactWithPatients == 'true';
    $('#patientForm').show();
  };
});