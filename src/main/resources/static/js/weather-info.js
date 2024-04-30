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

                const area = address.split(' ')[0]; // 지역명에서 앞의 두 단어 추출
                const city = address.split(' ')[1].slice(0, 2); // 지역명에서 두 번째 단어의 앞 두 글자 추출

                // 주간 육상 예보
                fetch(`http://localhost:8080/api/v1/area-code/mid-land?area=${area}`)
                    .then(response => response.json())
                    .then(midLandData => {
                        const weatherList = document.getElementById('weekly-land-weather');
                        weatherList.innerHTML = '';

                        const landRegId = midLandData.code;

                        // 중도 Land 코드를 사용하여 해당 지역의 날씨 정보를 가져오는 요청 보내기
                        fetch(`http://localhost:8080/api/v1/weather/mid-land?regId=${landRegId}`)
                            .then(response => response.json())
                            .then(landWeatherData => {
                                // 중도 지역의 날씨 정보를 바로 동적으로 추가
                                createWeeklyLandWeather(landWeatherData);

                            })
                            .catch(error => console.error('Error fetching mid land weather:', error));
                    })
                    .catch(error => console.error('Error fetching mid land TA code:', error));


                // 주간 기온 예보
                fetch(`http://localhost:8080/api/v1/area-code/mid-ta?area=${city}`)
                    .then(response => response.json())
                    .then(midTaData => {
                        const weatherList = document.getElementById('weekly-ta-weather');
                        weatherList.innerHTML = '';

                        const taRegId = midTaData.code;

                        fetch(`http://localhost:8080/api/v1/weather/mid-ta?regId=${taRegId}`)
                            .then(response => response.json())
                            .then(taWeatherData => {
                                // 중도 지역의 날씨 정보를 바로 동적으로 추가
                                createWeeklyTaWeather(taWeatherData);
                            })
                            .catch(error => console.error('Error fetching mid land weather:', error));
                    })
                    .catch(error => console.error('Error fetching mid land TA code:', error));

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
                                const baseDate = data.baseDate;
                                const baseTime = data.baseTime;

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

                                const nowcastValue = data.nowcastValue;

                                // 기온 표시
                                Object.entries(nowcastValue).forEach(([category, value]) => {
                                    // T1H에 해당하는 값만 따로 추출하여 id가 t1h인 span 태그에 할당
                                    if (category === 'T1H') {
                                        const t1hElement = document.getElementById('t1h-value');
                                        const replacedValue = replaceCategoryValue(category, value);
                                        t1hElement.textContent = `${replacedValue}`;
                                    }
                                });

                                // 강수 형태, 습도, 풍속 표시
                                const categoriesToShow = ['REH', 'PTY']; // 습도, 풍속, 강수 형태 카테고리
                                Object.entries(nowcastValue).forEach(([category, value]) => {
                                    // categoriesToShow 배열에 있는 카테고리에 해당하는 값만 처리
                                    if (categoriesToShow.includes(category)) {
                                        const listItem = document.createElement('li');
                                        const replacedCategory = replaceCategory(category);
                                        const replacedValue = replaceCategoryValue(category, value);
                                        listItem.textContent = `${replacedCategory}: ${replacedValue}`;
                                        weatherList.appendChild(listItem);
                                    }
                                });

                                fetch(`http://localhost:8080/api/v1/weather/by-hour?nx=${nx}&ny=${ny}`)
                                    .then(response => response.json())
                                    .then(data => {
                                        const weatherList = document.getElementById('six-hour-weather-list');
                                        weatherList.innerHTML = ''; // 이전에 표시된 날씨 정보 지우기

                                        // 날씨 정보를 화면에 동적으로 표시하기
                                        data.forEach(item => {
                                            const fcstTime = item.fcstTime;
                                            const forecastValues = item.forecastValues;

                                            // 오전과 오후를 계산하여 시간대 표시
                                            const hour = parseInt(fcstTime.slice(0, 2));
                                            const ampm = hour < 12 ? '오전' : '오후';
                                            const displayHour = hour % 12 || 12; // 12시를 0시로 표시하지 않기 위해

                                            // 시간대 별로 묶어서 화면에 표시하기
                                            let htmlContent = `<h5>${ampm} ${displayHour}시</h5>`;

                                            // 기온(T1H)와 하늘 상태(SKY) 표시
                                            ['T1H', 'SKY'].forEach(key => {
                                                const value = forecastValues[key];
                                                if (value !== undefined) {
                                                    const replacedValue = replaceCategoryValue(key, value);
                                                    htmlContent += `<span>${replacedValue} </span>`;
                                                }
                                            });

                                            const listItem = document.createElement('div');
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


// "내위치" 링크를 클릭하면 getCurrentWeather 함수 호출
document.getElementById('current-location-link').addEventListener('click', getCurrentWeather);



function getDayName(dayIndex) {
    const days = ['일', '월', '화', '수', '목', '금', '토'];
    return days[dayIndex];
}

function calculateDay(date, daysToAdd) {
    const newDate = new Date(date);
    newDate.setDate(newDate.getDate() + daysToAdd);
    const dayIndex = newDate.getDay();
    const dayName = getDayName(dayIndex);
    return `${dayName}`;
}

function createWeeklyLandWeather(weatherData) {
    const item = weatherData.response.body.items.item[0];
    const specialIcon = document.getElementById('t1h-icon');
    const weeklyWeatherList = document.getElementById('weekly-land-weather');
    const currentDate = new Date();

    specialIcon.innerHTML = '';
    weeklyWeatherList.innerHTML = '';

    for (let i = 3; i <= 10; i++) {
        const dayName = calculateDay(currentDate, i);
        const date = `<h5>${dayName}</h5>`;
        let wf;

        const listItem = document.createElement('span');

        // JavaScript 함수 내 이미지 요소에 CSS 클래스 추가
        if (i >= 8) {
            wf = item[`wf${i}`];
            listItem.classList.add('weather-item');

            // SVG 파일 선택
            const svg = getWeatherSVG(wf);

            // HTML에 이미지 추가
            listItem.innerHTML = `
            ${date}
            <img src="${svg}" alt="${wf}" class="weather-image" />
            <img src="${svg}" alt="${wf}" class="weather-image" />
            `;

        } else {
            const wfAm = item[`wf${i}Am`] || '정보 없음';
            const wfPm = item[`wf${i}Pm`] || '정보 없음';

            listItem.classList.add('weather-item');

            // SVG 파일 선택
            const amSvg = getWeatherSVG(wfAm);
            const pmSvg = getWeatherSVG(wfPm);

            if (i === 3) {
                // listItem에 아이콘 추가
                const amIcon = document.createElement('img');
                amIcon.src = amSvg;
                amIcon.alt = wfAm;
                amIcon.classList.add('weather-image'); // 클래스 추가
                specialIcon.appendChild(amIcon);
            }
            // HTML에 이미지 추가
            listItem.innerHTML = `
            ${date}
            <img src="${amSvg}" alt="${wfAm}" class="weather-image" />
            <img src="${pmSvg}" alt="${wfPm}" class="weather-image" />
            `;
        }


        weeklyWeatherList.appendChild(listItem);
    }
}

function createWeeklyTaWeather(weatherData) {
    const item = weatherData.response.body.items.item[0];
    const weeklyWeatherList = document.getElementById('weekly-ta-weather');
    const currentDate = new Date();

    for (let i = 3; i <= 10; i++) {
        const dayName = calculateDay(currentDate, i);
        const date = `<h5>${dayName}</h5>`;

        // 최저 기온과 최고 기온 정보만 가져옴
        const taMin = item[`taMin${i}`] || '정보 없음';
        const taMax = item[`taMax${i}`] || '정보 없음';

        const listItem = document.createElement('span');
        listItem.classList.add('weather-item');
        listItem.innerHTML = `
            <!-- <p>${date}</p> -->
            <p>${taMin}°C / ${taMax}°C</p>
        `;

        weeklyWeatherList.appendChild(listItem);
    }
}

// 날씨에 따라 해당하는 SVG 파일의 경로를 반환하는 함수
function getWeatherSVG(weather) {
    switch (weather) {
        case '맑음':
            return '/img/clear-day.svg';
        case '구름많음':
            return '/img/partly-cloudy-day.svg';
        case '구름많고 비':
            return '/img/partly-cloudy-rain-day.svg';
        case '구름많고 눈':
            return '/img/partly-cloudy-snow-day.svg';
        case '구름많고 비/눈':
            return '/img/partly-cloudy-sleet-day.svg';
        case '구름많고 소나기':
            return '/img/partly-cloudy-rain-day.svg';
        case '흐림':
            return '/img/overcast.svg';
        case '흐리고 비':
            return '/img/overcast-rain.svg';
        case '흐리고 눈':
            return '/img/overcast-snow.svg';
        case '흐리고 비/눈':
            return '/img/overcast-sleet.svg';
        case '흐리고 소나기':
            return '/img/overcast-rain.svg';
        default:
            return '/img/default.svg';
    }
}

