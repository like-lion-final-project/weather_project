// 이미지를 클릭하여 별점을 부여하는 함수
function rateImage(clickedImage) {
    // 클릭한 이미지의 src 속성값 가져오기
    var src = clickedImage.getAttribute('src');
    // 카테고리 값 가져오기
    var categoryInput = clickedImage.parentElement.querySelector('.category');
    var category = categoryInput.value;


    // 이전에 보여준 이미지 모두 제거
    var copiedImageContainer = document.getElementById('copied-image-container');
    copiedImageContainer.innerHTML = '';

    // 왼쪽에 새 이미지 엘리먼트 생성
    var copiedImage = document.createElement('img');
    copiedImage.src = src;

    // 별점 요소 생성
    var rating = document.createElement('div');
    rating.className = 'rating';
    for (let i = 4; i >= 0; i--) { // 5점부터 1점까지 역순으로 생성
        var star = document.createElement('span');
        star.setAttribute('data-rating', i + 1); // 데이터 값 변경

        var input = document.createElement('input');
        input.setAttribute('type', 'radio');
        input.setAttribute('name', 'rating');
        input.setAttribute('id', 'star' + (i + 1));
        input.value = i + 1;

        var label = document.createElement('label');
        label.setAttribute('for', 'star' + (i + 1));

        star.appendChild(input);
        star.appendChild(label);

        // 클로저를 사용하여 각 별점에 대한 정보를 전달
        star.onclick = function() { // 클로저를 사용하지 않고 직접 rateImage 함수 호출
            var index = i + 1;
            updateStarColor(index); // 클릭한 별점까지 색상 변경
            submitRating(src, index, category); // 이미지 URL과 별점을 서버에 전달
        };
        rating.appendChild(star);
    }

    // 왼쪽 이미지 컨테이너에 새 이미지와 별점 추가
    copiedImageContainer.appendChild(copiedImage);
    copiedImageContainer.appendChild(rating);
    copiedImageContainer.appendChild(category);
}
// 클릭한 별점까지의 색상을 변경하는 함수
function updateStarColor(clickedIndex) {
    var stars = document.querySelectorAll('.rating span');
    for (var i = 0; i < stars.length; i++) {
        var starIndex = parseInt(stars[i].getAttribute('data-rating'));
        if (starIndex <= clickedIndex) {
            stars[i].classList.add('active'); // .active 클래스 추가
        } else {
            stars[i].classList.remove('active'); // .active 클래스 제거
        }
    }
}

// 별점을 서버에 전송하는 함수
function submitRating(imageSrc, rating,category) {
    // 현재 페이지 URL에서 쿼리 문자열 추출
    var queryString = window.location.search;
    // 쿼리 문자열을 객체로 변환
    var queryParams = new URLSearchParams(queryString);
    // query 매개변수 값 가져오기
    var query = queryParams.get('query');
    // AJAX 요청 보내기
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/submit-rating", true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            console.log('Rating submitted successfully');
            alert("이미지에"+rating+"점을 부여 했습니다");
        }
    };
    var data = JSON.stringify({ image: imageSrc, rating: rating ,query: query,category:category });
    xhr.send(data);
}
