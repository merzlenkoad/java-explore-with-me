# java-explore-with-me
Template repository for ExploreWithMe project.

https://github.com/merzlenkoad/java-explore-with-me/pull/5

Свободное время — ценный ресурс. Ежедневно мы планируем, как его потратить — куда и с кем сходить.
Сложнее всего в таком планировании поиск информации и переговоры. Нужно учесть много деталей:
какие намечаются мероприятия, свободны ли в этот момент друзья, как всех пригласить и где собраться.

Данное приложение — это афиша,
в которой можно предложить какое-либо событие от выставки до похода в кино и собрать компанию для участия в нём.
___
Технологии : Maven, Spring-boot, jpa-hibernate, Docker, PostgreSQL, Lombok.
___

Реализация основного сервиса.
API основного сервиса разделено на три части:

- публичная доступна без регистрации любому пользователю сети;
- закрытая доступна только авторизованным пользователям;
- административная — для администраторов сервиса.

Публичный API предоставляет возможности поиска и фильтрации событий.
Закрытая часть API реализовывает возможности зарегистрированных пользователей продукта.
Административная часть API предоставляет возможности настройки и поддержки работы сервиса.

Более подробно с возможностями приложения можно ознакомиться в файле спецификации основного сервиса: [ewm-main-service-spec.json].

Реализация сервиса статистики.
Реализация HTTP-клиента для работы с сервисом статистики.

Функционал сервиса статистики содержит:

- запись информации о том, что был обработан запрос к эндпоинту API;
- предоставление статистики за выбранные даты по выбранному эндпоинту.

Более подробно с возможностями приложения можно ознакомиться в файле спецификации сервиса статистики [ewm-stats-service.json].
___
Проект разработал А.Д. Мерзленко.