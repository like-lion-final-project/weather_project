// 날씨 뉴스
let start = 1;
const newsList = document.getElementById('weather-news');

const currentTime = new Date();

// 시간 차이를 계산하고 표시 형식을 설정
function getTimeDifference(pubDate) {
    const articleTime = new Date(pubDate);
    const timeDiffMinutes = Math.floor((currentTime - articleTime) / (1000 * 60));

    if (timeDiffMinutes < 60) {
        return ` ${timeDiffMinutes}분 전`;
    } else {
        const timeDiffHours = Math.floor(timeDiffMinutes / 60);
        return ` ${timeDiffHours}시간 전`;
    }
}
// 날씨 뉴스 가져오기
function fetchWeatherNews(start) {
    fetch(`http://localhost:8080/api/v1/weather/news?start=${start}`)
        .then(response => response.json())
        .then(data => {
            data.items.forEach(item => {
                const newsContainer = document.createElement('div'); // 뉴스 목록을 감싸는 박스 요소
                newsContainer.classList.add('news-box'); // CSS 클래스 추가

                const title = document.createElement('a');
                title.href = item.link;
                title.innerHTML = item.title;

                const description = document.createElement('p');
                description.innerHTML = item.description;

                const pubDate = new Date(item.pubDate);
                const timeDifference = getTimeDifference(item.pubDate);

                const timeDifferenceElement = document.createElement('span');
                timeDifferenceElement.innerText = timeDifference;

                // 제목과 시간 차이를 포함하는 컨테이너에 추가
                newsContainer.appendChild(title);
                newsContainer.appendChild(timeDifferenceElement);
                newsContainer.appendChild(description);

                // 각 뉴스 기사마다 고유한 클래스를 추가하여 스타일링을 적용
                const uniqueClassName = `news-box-${start}-${data.items.indexOf(item)}`;
                newsContainer.classList.add(uniqueClassName);

                // 뉴스 목록을 감싸는 박스를 ul 대신에 바로 추가
                newsList.appendChild(newsContainer);

                // 뉴스 기사 사이에 수평선(hr 태그) 추가
                const hr = document.createElement('hr');
                newsList.appendChild(hr);
            });
        })
        .catch(error => console.error('날씨 뉴스를 가져오는 중 오류 발생:', error));
}

fetchWeatherNews(start);

function loadMoreNews() {
    start += 1;
    fetchWeatherNews(start);
}

