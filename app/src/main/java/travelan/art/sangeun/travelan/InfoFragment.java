package travelan.art.sangeun.travelan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import travelan.art.sangeun.travelan.adapters.InformationListAdapter;
import travelan.art.sangeun.travelan.models.Information;

/**
 * Created by sangeun on 2018-05-12.
 */

public class InfoFragment extends Fragment {
    private static final String TAG = "InfoFragment";
    private RecyclerView recyclerView;
    private InformationListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info,container,false);
        setHasOptionsMenu(true);
        List<Information> items = makeDummy();

        recyclerView = v.findViewById(R.id.infoList);
        adapter = new InformationListAdapter(items, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_information,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    public List makeDummy(){
        List<Information> list = new ArrayList<>();
        Information item01 = new Information();
        item01.title = "벨기에 여행 성수기 기간 중 여행객 물품 도난 주의 안내";
        item01.countryName = "Belgium";
        item01.wrtDt = "2018-06-01";
        item01.content="  벨기에 여행 성수기 기간 중 여행객 물품 도난 주의 안내   ○ 여행 성수기에 따라 벨기에에서 잦은 소매치기 및 여권, 물품 도난 등의 사건이 빈발하고 있습니다. 벨기에를 여행하시는 여행객 분들은 아래와 같은 도난범들의 주요 수법을 숙지하셔서 물품 도난 등의 피해가 발생하지 않도록 주의해 주시기 바랍니다.       [기차 여행시] - 기차 선반에 중요한 물품이 든 가방을 절대 올려두지 말것- 누군가가 창문을 두드려 시선을 끄는 경우 가방을 주의할 것- 기차안에서 동전을 흘려 주위를 끄는 경우 가방을 주의할 것- 중요한 물품이 든 가방은 좌석 옆 빈자리에 두지 말 것- (화장실 이용 등)잠시 자리를 비워야 하는 경우 가방을 두고 이동하지 말것      [호텔 숙박시] - 호텔 체크인, 아웃 시 지갑 등 가방을 주의 할 것- 호텔 조식 중 또는 카페, 바 이용시 가방을 의자에 걸어두지 말것- 뷔페 식사를 위해 가방을 좌석에 두고 다른 곳으로 이동하지 말것- 호텔 로비에 있는 소파 등에 가방을 두고 다른 곳으로 이동하지 말것    ○ 중요 물품이 있는 가방을 두고 사진 등을 촬영하거나 하는 경우 도난 발생의 확율이 높습니다. 여행경비용 현금 소지 시 한 곳에 모두 두지 말고 분산해 놓거나 복대 등을 착용하는 것도 한 방법입니다. 상기 자주 발생하는 도난의 유형이니 숙지하셔서 안전여행 하시기 바랍니다.   ";
        item01.flagImage="";

        Information item02 = new Information();
        item02.title = "성지순례 목적 사우디아라비아 방문시 유의사항";
        item02.countryName = "Saudi Arabia";
        item02.wrtDt = "2018-06-01";
        item02.content="○ 주재국 여권국(Al-Jawazat)에 따르면 성지순례를 목적으로 주재국을 방문하는 외국인들이 비자 유효기간이 만료된 후 초과 체류할 경우 5만SR(한화 약 1,436만원)의 벌금과 6개월 구속 처벌을 받게 됩니다. ○ 또한 성지순례를 목적으로 발급되는 Umrah비자의 경우, 메카, 젯다 및 메디나 내에서만 체류가 허용되며, 상기 내용들을 위반한 인물을 고용ㆍ보호하거나 또는 이동 편의 및 은신처를 제공하는 주재국 국민 및 거주 외국인들도 처벌 대상에 포함됩니다. ○ 사우디아라비아 성지순례를 목적으로 방문한 우리국민 또는 방문 계획이 있으신 분들께서는 상기 내용을 주지하시고 주재국 법에 엄격히 금지되어 있는 위반사항에 연루되지 않도록 각별히 주의하여 주시기 바랍니다. ○ 불의의 사고나 신변안전 관련한 사건 발생시 주사우디대사관 또는 영사콜센터로 연락하시기 바랍니다.    ☞ 주사우디대사관 : +966-11-488-2211 / (근무시간 외) +966-50-080-1065    ☞ 영사콜센터 : +82-2-3210-0404";
        item02.flagImage="";

        Information item03 = new Information();
        item03.title = "신변 안전 안내 : 가자지구에서 로켓포 공격";
        item03.countryName = "Israel";
        item03.wrtDt = "2018-06-01";
        item03.content="신변 안전 안내 : 가자지구에서 로켓포 공격○ 2018.5.29.(화) 오전 7시경 가자지구에서 이스라엘 남부지역(스데롯, 아슈켈론 등)으로 박격포 25개가 발사된 데 이어 로켓포 3발이 발사되는 등 총 30발의 로켓포 공격이 4차례 있었습니다.○ 이에 대응하여 이스라엘군은 가자지구내 하마스와 Islamic Jihad 목표물 30여 곳을 공습ㆍ타격하는 등 2014년 Operation Protective Edge 작전 이래 최대 규모로 대응 공격을 했습니다.○ 5.14(월) 미 대사관 예루살렘 이전 후 최근에는 다소 소강상태를 보여 왔으나, 금번 로켓포 공격으로 인해 이-팔간 분쟁이 재발될 여지가 있는 바, 이스라엘 거주 및 여행 중이거나 여행을 계획하시는 우리국민들께서는 이스라엘 남부지역(가자 인근) 여행을 자제해 주시기 바랍니다.○ 불의의 사고나 신변안전 관련한 사건 발생시 주이스라엘대사관 또는 영사콜센터로 연락하시기 바랍니다.   ☞ 주이스라엘대사관 : +972-(0)9-959-6826 / (당직번호)+972-(0)50-883-9479, +971-(0)50-641-3026, +971-(0)50-804-9144    ☞ 영사콜센터 : +82-2-3210-0404";
        item03.flagImage="";

        Information item04 = new Information();
        item04.title = "브라질 내 황열 확산 관련 안전 유의 안내";
        item04.countryName = "Brazil";
        item04.wrtDt = "2018-05-31";
        item04.content="  브라질 내 황열 확산 관련 안전 유의 안내   ○ 브라질 보건부는 최근 브라질 내 황열 관련 환자가 2017.7.1.부터 2018.05.16까지 의심환자 6,589명이 발생하여, 그 중 확진으로 판명된 사람이 1,266명, 사망한 사람이 415명에 달한다고 발표했습니다.    ※ 황열은 브라질 내에서 주기적으로 발병하는 풍토병으로 매년 12월~5월 간 주로 발병  \n" +
                "  발생지역\n" +
                "  발생현황\n" +
                " \n" +
                "  Sao Paulo(상파울루주)\n" +
                "  의심환자 2,885명(확진자 516명, 사망자 163명)\n" +
                " \n" +
                "  Minas Gerais(미나스제라이스주)\n" +
                "  의심환자 1,593명(확진자 520명, 사망자 177명)\n" +
                " \n" +
                "  Rio de Janeiro(히우데자네이루주)\n" +
                "  의심환자 1,346명(확진자 223명, 사망자 73명)\n" +
                " \n" +
                "  Espirto Santo(에스피리투산투주)\n" +
                "  의심환자 127명(확진자 6명, 사망자 1명)\n" +
                " \n" +
                "  Distrito Federal(연방구)\n" +
                "  의심환자 81명(확진자 1명, 사망자 1명)\n" +
                " \n" +
                "  Paraná(파라나주)등 기타 주\n" +
                "  의심환자 557명(확진자 0명, 사망자 0명)\n" +
                " \n" +
                " \n" +
                "  - 브라질 보건당국은 2017년 동기간과 비교하여 황열 확진자 및 사망자가 큰 폭으로 증가 추세를 보이자 2018.3.20일부로 브라질 전역에 황열 예방 접종을 권고하며 백신 보급 확대에 적극적인 조치를 취하고 있으나, 당분간은 황열 발병이 지속 증가할 것으로 예상    ○ 황열은 백신접종을 통해 예방이 가능하고 1회 접종 시 평생 면역력을 획득하므로 브라질을 방문 예정이신 우리국민들께서는 출국 10일 전까지 반드시 백신 접종을 받으시기 바라며 현지 체류 교민들께서도 예방 접종을 해주시기 바랍니다. 또한, 가급적 긴 옷을 입고 모기기피제를 적절히 사용하며, 방충망이 있는 숙소를 이용하는 등 황열 바이러스를 보유한 모기(Aedes 또는 Haemogogus 속)에 물리지 않도록 만전을 기해주시기 바랍니다. ※ 황열 백신 접종기관 : 우리나라 전국 13개 국립검역소 및 26개 국제공인 예방접종 지정의료기관(질병관리본부 홈페이지(http://www.cdc.go.kr) 참조)   ○ 아울러, 위급상황 발생시 주브라질대사관(+55 61 3321 2500 / 근무시간 외 : +55 61 99658 2421) 또는 영사콜센터(+82-2-3210-0404)로 연락하여 주시기 바랍니다.   붙임 : 황열 관련 중남미 국가별 출입국 통제 현황 및 질병관리본부 참고자료   ";
        item04.flagImage="";

        list.add(item01);
        list.add(item02);
        list.add(item03);
        list.add(item04);

        return list;
    }
}
