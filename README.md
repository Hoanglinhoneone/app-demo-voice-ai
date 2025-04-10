# 🎙️ Voice-AI - Trợ lý ảo thông minh

Ứng dụng Android trò chuyện với AI bằng giọng nói, hỗ trợ chuyển đổi hai chiều văn bản - âm thanh.  
**Yêu cầu:** Android 10+ (API 29) và API Key từ Gemini.

---

## 🌟 Tính năng nổi bật
- 💬 Trò chuyện thời gian thực với AI (Gemini LLM)
- 🎙️ Nhận diện giọng nói → văn bản (SpeechToText)
- 📢 Phát lại phản hồi AI bằng giọng nói (TextToSpeech)
- 🚀 Tối ưu hóa cho thiết bị di động

---

## 🛠 Công nghệ sử dụng
| Category       | Công nghệ               | Mục đích                          |
|----------------|-------------------------|-----------------------------------|
| **AI Core**    | Gemini (GenerativeModel) | Xử lý ngôn ngữ tự nhiên          |
| **Voice**      | SpeechToText/TextToSpeech| Chuyển đổi giọng nói ↔ văn bản   |
| **Architecture**| MVVM + Clean Architecture| Tách biệt layers rõ ràng         |
| **UI**         | Jetpack Compose + Material 3 | Giao diện hiện đại         |
| **Networking** | Retrofit + OkHttp        | Giao tiếp API                    |
| **DI**         | Hilt                    | Quản lý dependencies             |
| **Utilities**  | Coil, Timber            | Tải ảnh & logging                |

---

## 🚀 Cài đặt
### Yêu cầu
- Android Studio IDE
- Android 10 (API 29) trở lên
- API Key Gemini (xem [hướng dẫn lấy key](https://ai.google.dev/))
- Domain API
### Các bước
1. Clone repo:
   ```bash
   git clone https://github.com/your-repo/voice-ai.git
