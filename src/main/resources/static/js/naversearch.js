// 이미지를 클릭하여 별점을 부여하는 함수
function rateImage(clickedImage) {
    var src = clickedImage.getAttribute('src');

    // 카테고리 값 가져오기
    var category1Input = clickedImage.parentElement.querySelector('.category1');
    var category2Input = clickedImage.parentElement.querySelector('.category2');
    var category3Input = clickedImage.parentElement.querySelector('.category3');
    var category4Input = clickedImage.parentElement.querySelector('.category4');

    // 각 카테고리 값
    var category1 = category1Input.value;
    var category2 = category2Input.value;
    var category3 = category3Input.value;
    var category4 = category4Input.value;

    // 쿼리 값 가져오기
    var queryInput = clickedImage.parentElement.querySelector('.query');
    var query = queryInput.value;

    // 쿼리가 포함된 카테고리 찾기
    var selectedCategory = null;

    if (category1.includes(query)) {
        selectedCategory = category1;
    } else if (category2.includes(query)) {
        selectedCategory = category2;
    } else if (category3.includes(query)) {
        selectedCategory = category3;
    } else if (category4.includes(query)) {
        selectedCategory = category4;
    } else {
        // 쿼리를 카테고리로 사용
        selectedCategory = query;
    }

    // 이전에 보여준 이미지 모두 제거
    var copiedImageContainer = document.getElementById('copied-image-container');
    copiedImageContainer.innerHTML = '';

    // 왼쪽에 새 이미지 엘리먼트 생성
    var copiedImage = document.createElement('img');
    copiedImage.src = src;

    // 별점 요소 생성
    var rating = document.createElement('div');
    rating.className = 'rating';

    // 별점 생성
    for (let i = 4; i >= 0; i--) {
        var star = document.createElement('span');
        star.setAttribute('data-rating', i + 1);

        var input = document.createElement('input');
        input.setAttribute('type', 'radio');
        input.setAttribute('name', 'rating');
        input.setAttribute('id', 'star' + (i + 1));
        input.value = i + 1;

        var label = document.createElement('label');
        label.setAttribute('for', 'star' + (i + 1));

        star.appendChild(input);
        star.appendChild(label);

        star.onclick = function() {
            var index = i + 1;
            updateStarColor(index);
            submitRating(src, index, selectedCategory); // 선택된 카테고리 전달
        };

        rating.appendChild(star);
    }

    // 왼쪽 이미지 컨테이너에 새 이미지와 별점 추가
    copiedImageContainer.appendChild(copiedImage);
    copiedImageContainer.appendChild(rating);
}

// 별점을 서버에 전송하는 함수
function submitRating(imageSrc, rating, category) {
    var queryString = window.location.search;
    var queryParams = new URLSearchParams(queryString);
    var query = queryParams.get('query');

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/submit-rating", true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            console.log('Rating submitted successfully');
            alert("이미지에 " + rating + "점을 부여하였고, 카테고리는 " + category + "입니다.");
        }
    };

    var data = JSON.stringify({
        image: imageSrc,
        rating: rating,
        category: category
    });

    xhr.send(data);
}

function updateStarColor(clickedIndex) {
    var stars = document.querySelectorAll('.rating span');
    for (var i = 0; i < stars.length; i++) {
        var starIndex = parseInt(stars[i].getAttribute('data-rating'));
        if (starIndex <= clickedIndex) {
            stars[i].classList.add('active');
        } else {
            stars[i].classList.remove('active');
        }
    }
}