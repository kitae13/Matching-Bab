package com.example.matchingbab.global.config;

import com.example.matchingbab.guide.entity.Guide;
import com.example.matchingbab.guide.repository.GuideRepository;
import com.example.matchingbab.guide.type.GuideCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class QuestionDataInitializer
        implements CommandLineRunner {

    private final GuideRepository guideRepository;

    @Override
    public void run(String... args) {
        saveGuide(
                GuideCategory.MANNER,
                "밥약 신청은 어떻게 하나요?",
                "선배에게 부담을 주지 않고 정중하게 밥약을 신청하는 방법입니다.",
                """
                1. 간단하게 자신을 소개해주세요.
                2. 밥약을 신청한 이유를 말해주세요.
                3. 선배가 가능한 시간을 선택할 수 있도록 여유 있게 요청해주세요.
                4. 답변을 재촉하거나 반복해서 연락하지 마세요.

                신청 예시:
                안녕하세요 선배님! 컴퓨터공학과 1학년 홍길동입니다.
                진로와 수강신청에 관해 궁금한 점이 있어 밥약을 신청드립니다.
                편하신 시간이 있으시면 알려주시면 감사하겠습니다.
                """,
                1
        );

        saveGuide(
                GuideCategory.PROCESS,
                "밥약 전 무엇을 준비해야 하나요?",
                "약속 날짜, 장소와 질문을 미리 확인하는 방법입니다.",
                """
                1. 약속 날짜와 시간을 다시 확인해주세요.
                2. 약속 장소의 위치를 미리 확인해주세요.
                3. 궁금한 질문을 2~3개 정도 준비해주세요.
                4. 늦을 것 같다면 약속 시간 전에 상대방에게 알려주세요.
                """,
                2
        );

        saveGuide(
                GuideCategory.MANNER,
                "밥약에서 피하면 좋은 질문",
                "처음 만난 상대방에게 부담이 될 수 있는 질문을 안내합니다.",
                """
                학점, 연봉, 가정환경, 연애 등 지나치게 개인적인 질문은 피해주세요.
                상대방이 대답하기 어려워하면 다른 주제로 자연스럽게 넘어가 주세요.
                조언을 강요하거나 상대방의 선택을 평가하지 않는 것이 좋습니다.
                """,
                3
        );

        saveGuide(
                GuideCategory.SAFETY,
                "안전한 밥약을 위한 수칙",
                "개인정보를 보호하고 안전한 장소에서 만나는 방법입니다.",
                """
                1. 첫 만남은 학교 주변의 공개된 식당이나 카페를 이용해주세요.
                2. 주소, 비밀번호, 금융정보 등 민감한 정보는 공유하지 마세요.
                3. 금전 요구나 불쾌한 행동이 발생하면 대화를 중단해주세요.
                4. 문제가 발생하면 서비스의 신고 기능을 이용해주세요.
                """,
                4
        );

        saveGuide(
                GuideCategory.THANKS,
                "밥약 후 감사 인사 보내기",
                "밥약이 끝난 후 감사의 마음을 표현하는 방법입니다.",
                """
                밥약 후에는 짧은 감사 메시지를 보내는 것이 좋습니다.

                예시:
                오늘 시간 내주셔서 감사합니다!
                말씀해주신 진로와 수강신청 조언이 정말 도움이 됐습니다.

                서비스의 보은 버튼을 이용해 고마움을 표현할 수도 있습니다.
                """,
                5
        );
    }

    private void saveGuide(
            GuideCategory category,
            String title,
            String summary,
            String content,
            int displayOrder
    ) {
        if (!guideRepository.existsByTitle(title)) {
            guideRepository.save(
                    Guide.create(
                            category,
                            title,
                            summary,
                            content,
                            displayOrder
                    )
            );
        }
    }
}