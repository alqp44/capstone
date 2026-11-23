# capstone

MyNotificationListener.kt
NotificationListenerService를 상속받아서 안드로이드 시스템에 올라오는 알림을 가로채고 파싱하는 파일
override fun onNotificationPosted(sbn: StatusBarNotification) { — 기기에 새 알림이 뜨거나 갱신될 때마다 이 함수를 호출함
sbn 안에 알림 객체와 발신 앱 정보 등이 들어 있음
val regex = Regex("""(.+?)\s+([\d,]+)원""") — "공백 전까지의 가맹점명 + 공백 + 콤마 포함 숫자 + '원'" 패턴을 찾는 정규식 객체를 만듦
정규식 객체와 일치하는 부분을 찾아서 뽑아냄 / 일치하지 않으면 결제 알림이 아니라고 보고 종료
val amount = match.groupValues[2].replace(",", "").toIntOrNull() ?: return — 두 번째 괄호 ([\d,]+)에 잡힌 금액에서 쉼표(,)를 지운 뒤 숫자로 변환
PaymentStore.addPayment(merchant, amount) — 뽑아낸 가맹점과 금액을 저장소(PaymentStore)에 넘겨서 리스트에 쌓이게 함.

PaymentStore.kt
data class PaymentItem(val merchant: String, val amount: Int, val time: String) — 결제 1건의 데이터를 담는 틀(가맹점명, 금액, 시간)fun addPayment(merchant: String, amount: Int, time: String = currentTime()) { — 새 결제 내역을 리스트에 추가하는 함수
시간을 안 넘기면 기본값으로 현재 시각을 넣음
_payments.update { currentList -> listOf(PaymentItem(merchant, amount, time)) + currentList } — 기존 리스트를 직접 수정하지 않고,
새 항목을 맨 앞에 붙인 새 리스트로 교체해서 상태 변경을 확실히 알리는 코드
이게 없으면 새로 화면을 그릴 때 계속 초기화되어 하나만 나오게됨
