$(document).ready(function() {
     const today = new Date();

     // Get date components
     const day = today.getDate();
     const month = today.toLocaleString('en-US', { month: 'long' });
     const year = today.getFullYear();

     // Get time components
     let hour = today.getHours();
     const minute = today.getMinutes();
     const ampm = hour >= 12 ? 'pm' : 'am';
     hour %= 12;
     hour = hour || 12;
     const time = `${hour}:${minute.toString().padStart(2, '0')} ${ampm}`;

     // Format date and time
     const formattedDate = `${day}th of ${month}, ${year}`;
     const formattedTime = `${time}`;

     const daysOfWeek = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
     const dayOfWeek = today.getDay();
     const dayName = daysOfWeek[dayOfWeek];
     const datePhrase = "This document was generated on the ";

    // Set date and time in span tag
    const dateTimeSpan = document.getElementById("date-time");
    dateTimeSpan.textContent = `${datePhrase} ${formattedDate} at ${formattedTime}, on a ${dayName}.`;

     // Output formatted date and time
     console.log(`Today is ${dayName} ${formattedDate} and the current time is ${formattedTime}`);

})