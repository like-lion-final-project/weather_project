// 날씨 뉴스
let start = 1;
const newsList = document.getElementById('weather-news');

const currentTime = new Date();

// 시간 차이를 계산하고 표시 형식을 설정
function getTimeDifference(pubDate) {
    const articleTime = new Date(pubDate);
    const timeDiffMinutes = Math.floor((currentTime - articleTime) / (1000 * 60));

    if (timeDiffMinutes < 60) {
        return `${timeDiffMinutes}분 전`;
    } else {
        const timeDiffHours = Math.floor(timeDiffMinutes / 60);
        return `${timeDiffHours}시간 전`;
    }
}
// 날씨 뉴스 가져오기
function fetchWeatherNews(start) {
    fetch(`http://localhost:8080/api/v1/weather/news?start=${start}`)
        .then(response => response.json())
        .then(data => {
            data.items.forEach(item => {
                const li = document.createElement('li');

                const title = document.createElement('h4');
                title.innerHTML = item.title;

                const description = document.createElement('p');
                description.innerHTML = item.description;

                const pubDate = new Date(item.pubDate);
                const timeDiff = Math.floor((currentTime - pubDate) / (1000 * 60));

                const timeDifferenceElement = document.createElement('span');
                timeDifferenceElement.innerText = getTimeDifference(item.pubDate);

                li.appendChild(title);
                li.appendChild(description);
                li.appendChild(timeDifferenceElement);

                newsList.appendChild(li);
            });
        })
        .catch(error => console.error('날씨 뉴스를 가져오는 중 오류 발생:', error));
}

fetchWeatherNews(start);

function loadMoreNews() {
    start += 1;
    fetchWeatherNews(start);
}