// 각 카테고리에 대한 값 대체 함수
function replaceCategoryValue(category, value) {
    switch (category) {
        case 'SKY':
            return {
                1: '맑음',
                3: '구름많음',
                4: '흐림'
            }[value] || '알 수 없음';
        case 'PTY':
            return {
                0: '강수없음',
                1: '비',
                2: '비/눈',
                3: '눈',
                5: '빗방울',
                6: '빗방울눈날림',
                7: '눈날림'
            }[value] || '알 수 없음';
        case 'RN1':
            return value === 0 ? '강수없음' : `${value}mm`;
        case 'REH':
            return `${value}%`;
        case 'T1H':
            return `${value}°C`;
        case 'UUU':
        case 'VVV':
            return `${value}m/s`;
        case 'VEC':
            return `${value}°`;
        case 'WSD':
            return `${value}m/s`;
        case 'LGT':
            return value === 0 ? '없음' : '있음';
        default:
            return value;
    }
}

// 날씨 카테고리를 한글로 대체하는 함수
function replaceCategory(category) {
    switch (category) {
        case 'SKY':
            return '하늘상태';
        case 'PTY':
            return '강수 형태';
        case 'RN1':
            return '1시간 강수량';
        case 'REH':
            return '습도';
        case 'UUU':
            return '동서방향 풍속';
        case 'VEC':
            return '풍향';
        case 'VVV':
            return '남북방향 풍속';
        case 'LGT':
            return '낙뢰';
        case 'WSD':
            return '풍속';
        case 'T1H':
            return '기온';
        default:
            return category;
    }
}

// 사용자의 현재 위치 정보를 얻어와서 날씨 정보를 조회하는 함수
function getCurrentWeather() {
    navigator.geolocation.getCurrentPosition(function (position) {
        var latitude = position.coords.latitude;
        var longitude = position.coords.longitude;

        // 지역명 표시
        fetch(`http://localhost:8080/api/v1/weather/rgeocode?lat=${latitude}&lng=${longitude}`)
            .then(response => response.json())
            .then(data => {
                const address = data.address.replace(/^kr\s*/, ''); // "kr" 제거
                document.getElementById('location').textContent = `${address}`;

                // 격자 XY 변환
                fetch(`http://localhost:8080/api/v1/weather/convert-grid?lat=${latitude}&lng=${longitude}`)
                    .then(response => response.json())
                    .then(data => {
                        const nx = data.nx;
                        const ny = data.ny;

                        // 초단기 실황
                        fetch(`http://localhost:8080/api/v1/weather/by-current?nx=${nx}&ny=${ny}`)
                            .then(response => response.json())
                            .then(data => {
                                // 기준 날짜와 시간 정보 추출
                                const baseDate = data.response.body.items.item[0].baseDate;
                                const baseTime = data.response.body.items.item[0].baseTime;

                                // 기준 시간을 시, 분으로 분할
                                const baseHour = parseInt(baseTime.substring(0, 2));
                                const ampm = baseHour >= 12 ? '오후' : '오전';
                                const displayHour = baseHour % 12 || 12;

                                // 기준 날짜를 형식에 맞게 변환 (YYYY. MM. DD)
                                const formattedDate = `${baseDate.slice(0, 4)}. ${baseDate.slice(4, 6)}. ${baseDate.slice(6, 8)}`;

                                // 결과를 HTML에 적용
                                document.getElementById('base-date').textContent = `${formattedDate} ${ampm} ${displayHour}시 기준`;

                                const weatherList = document.getElementById('weather-list');
                                weatherList.innerHTML = ''; // 이전에 표시된 날씨 정보 지우기

                                data.response.body.items.item.forEach(item => {
                                    const listItem = document.createElement('li');
                                    const category = replaceCategory(item.category);
                                    const value = replaceCategoryValue(item.category, item.obsrValue); // 각 카테고리에 대한 값 대체
                                    listItem.textContent = `${category}: ${value}`;
                                    weatherList.appendChild(listItem);
                                });


                                // 날씨 정보 요청
                                fetch(`http://localhost:8080/api/v1/weather/by-hour?nx=${nx}&ny=${ny}`)
                                    .then(response => response.json())
                                    .then(data => {
                                        const weatherList = document.getElementById('six-hour-weather-list');
                                        weatherList.innerHTML = ''; // 이전에 표시된 날씨 정보 지우기

                                        // 날씨 정보를 화면에 동적으로 표시하기
                                        data.forEach(item => {
                                            const listItem = document.createElement('li');
                                            const fcstTime = item.fcstTime;
                                            const forecastValues = item.forecastValues;

                                            // 시간대 별로 묶어서 화면에 표시하기
                                            let htmlContent = `<h5>${fcstTime.slice(0, 2)}:${fcstTime.slice(2)}</h5>`;
                                            htmlContent += '<ul>';

                                            // forecastValues에 대한 정보 표시
                                            for (const [key, value] of Object.entries(forecastValues)) {
                                                const replacedCategory = replaceCategory(key); // 카테고리 한글로 대체
                                                const replacedValue = replaceCategoryValue(key, value); // 값 대체
                                                htmlContent += `<li>${replacedCategory}: ${replacedValue}</li>`;
                                            }

                                            htmlContent += '</ul>';
                                            listItem.innerHTML = htmlContent;

                                            weatherList.appendChild(listItem);
                                        });
                                    })
                                    .catch(error => console.error('날씨 정보를 가져오는 중 오류 발생:', error));
                            })
                            .catch(error => console.error('날씨 데이터를 가져오는 중 오류 발생:', error));
                    })
                    .catch(error => console.error('격자 좌표를 가져오는 중 오류 발생:', error));
            }, function (error) {
                console.error("날씨 데이터를 가져오는 중 오류 발생:", error);
            });
    });
}


// 페이지 로드 시 사용자의 현재 위치 정보를 이용하여 날씨 정보 조회
document.addEventListener('DOMContentLoaded', getCurrentWeather);