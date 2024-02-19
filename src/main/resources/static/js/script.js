document.addEventListener("DOMContentLoaded", function () {
  const inputs = document.querySelector(".inputs"),
    hintTag = document.querySelector(".hint span"),
    guessLeft = document.querySelector(".guess-left span"),
    wrongLetter = document.querySelector(".wrong-letter span"),
    resetBtn = document.querySelector(".reset-btn"),
    typingInput = document.querySelector(".typing-input");
  const popupDiv = document.querySelector(".popup");

  let maxGuesses, incorrectLetters = [], correctLetters = [];
  let word = document.getElementById("vocab").innerHTML;
  let hint = document.getElementById("vocabH").innerHTML;
  let score = document.getElementById("sc").innerHTML;
  let id = document.getElementById("userId").innerHTML;

  function randomWord() {
    maxGuesses = word.length >= 5 ? 5 : 5;
    correctLetters = [];
    incorrectLetters = [];
    hintTag.innerText = hint;
    guessLeft.innerText = maxGuesses;
    wrongLetter.innerText = incorrectLetters;

    let html = "";
    for (let i = 0; i < word.length; i++) {
      html += `<input type="text" disabled>`;
    }
    inputs.innerHTML = html;
  }
  randomWord();

  function showPopupMessage(message) {
    popupDiv.innerHTML = `<div class="popup-content">${message}</div>`;
    popupDiv.style.display = "block";

    // Close the pop-up after 2 seconds
    setTimeout(() => {
        popupDiv.style.display = "none";
        if (message.includes("Congrats!") || message.includes("Game over!")) {
          if (message.includes("Congrats!")) {
            updateScoreInController(); // Increase score on winning and update in the database
          }
          else{
            reloadPage();
          }
        }
      }, 2000);
    }

     function reloadPage(){
        const form = document.createElement('form');
         form.action = '/reload';
         form.method = 'POST';

        form.setAttribute('th:object', '${user}');

        // Include user ID in the form data
        const userIdInput = document.createElement('input');
        userIdInput.type = 'hidden';
        userIdInput.setAttribute('th:field', '*{id}');
        userIdInput.value = id;
        form.appendChild(userIdInput);

         document.body.appendChild(form);
         form.submit();
     }

     function updateScoreInController() {
         const form = document.createElement('form');
         form.action = '/updateScore';
         form.method = 'POST';

        form.setAttribute('th:object', '${user}');

        // Include user ID in the form data
        const userIdInput = document.createElement('input');
        userIdInput.type = 'hidden';
        userIdInput.setAttribute('th:field', '*{id}');
        userIdInput.value = id;
        form.appendChild(userIdInput);

         document.body.appendChild(form);
         form.submit();
     }




  function initGame(e) {
    let key = e.target.value.toLowerCase();
    if (key.match(/^[A-Za-z]+$/) && !incorrectLetters.includes(` ${key}`) && !correctLetters.includes(key)) {
      if (word.includes(key)) {
        for (let i = 0; i < word.length; i++) {
          if (word[i] == key) {
            correctLetters += key;
            inputs.querySelectorAll("input")[i].value = key;
          }
        }
      } else {
        maxGuesses--;
        incorrectLetters.push(` ${key}`);
      }
      guessLeft.innerText = maxGuesses;
      wrongLetter.innerText = incorrectLetters;
    }
    typingInput.value = "";

    setTimeout(() => {
      if (correctLetters.length === word.length) {
        showPopupMessage(`Congrats! You found the word: ${word.toUpperCase()}`);
        return randomWord();
      } else if (maxGuesses < 1) {
        showPopupMessage(`Game over! The word was: ${word.toUpperCase()}`);
        for (let i = 0; i < word.length; i++) {
          inputs.querySelectorAll("input")[i].value = word[i];
        }
      }
    }, 1000);
  }

  resetBtn.addEventListener("click", function () {
    location.reload();
  });
  typingInput.addEventListener("input", initGame);
  inputs.addEventListener("click", () => typingInput.focus());
  document.addEventListener("keydown", () => typingInput.focus());
});