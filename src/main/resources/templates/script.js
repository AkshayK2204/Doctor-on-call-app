function updateStartTimeOptions(selectedDate) {
    const startTimeSelect = document.getElementById('startTime');

    startTimeSelect.innerHTML = '<option value="" disabled selected>Select Start Time</option>';

    if (openAppointmentsMap[selectedDate]) {
        openAppointmentsMap[selectedDate].forEach(time => {
            const option = document.createElement('option');
            option.value = time;
            option.textContent = time;
            startTimeSelect.appendChild(option);
        });
    }
}
