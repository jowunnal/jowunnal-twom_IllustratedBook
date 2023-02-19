package com.jinproject.twomillustratedbook.ui.screen.droplist.monster

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinproject.twomillustratedbook.ui.screen.alarm.item.AlarmItem
import com.jinproject.twomillustratedbook.data.repository.DropListRepository
import com.jinproject.twomillustratedbook.data.database.Entity.Monster
import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.item.ItemState
import com.jinproject.twomillustratedbook.ui.screen.droplist.monster.item.MonsterState
import com.jinproject.twomillustratedbook.utils.getMonsterCode
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DropListUiState(
    val monster: List<MonsterState>
) {
    companion object {
        fun getInitValue() = DropListUiState(
            monster = emptyList()
        )
    }
}

@HiltViewModel
class DropListViewModel @Inject constructor(
    private val repository: DropListRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(DropListUiState.getInitValue())
    val uiState get() = _uiState.asStateFlow()


    fun getMonsterListFromMap(map: String) =
        repository.getMonsterListFromMap(map).onEach { monsterModelList ->
            _uiState.update { state ->
                state.copy(monster = monsterModelList.map { monsterModel ->
                    monsterModel.toMonsterState()
                }.sortedWith(compareBy { monsterState -> monsterState.level }))
            }
        }.catch { e ->
            Log.d("test", "exception : ${e.message}\n ${e.printStackTrace()}")
        }.launchIn(viewModelScope)

    fun itemListToSingleString(itemList: List<ItemState>): String {
        var contents = ""
        itemList.forEachIndexed { index, item ->
            contents += if (itemList.lastIndex != index) "${item.name}, " else item.name
        }
        return contents
    }




    /*리펙토링중*/
    lateinit var monster: Monster
    private var selectedBossListSharedPref =
        context.getSharedPreferences("bossList", Context.MODE_PRIVATE)
    var selectedBossList = MutableLiveData<List<String>>(
        selectedBossListSharedPref.getStringSet("boss", null)?.toList() ?: emptyList()
    )
    var selectedBossItem = ""

    /*
    suspend fun checkIsClickedBoss(pos: Int, inputData: String) {
        when (clickable) {
            -1 -> {
                clickable = pos
                monster = getMonsInfo(inputData)
                alarmItem = AlarmItem(
                    monster.monsName,
                    monster.monsImgName,
                    getMonsterCode(monster.monsName),
                    monster.monsGtime
                )
            }
            pos -> {
                clickable = -1
            }
            else -> {
                Toast.makeText(
                    context.applicationContext,
                    "동시에 2개이상을 선택할수 없습니다. 해제후 다시선택해주세요",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }*/

    fun getBossList() {
        viewModelScope.launch(Dispatchers.Main) {
            selectedBossList.value =
                selectedBossListSharedPref.getStringSet("boss", null)!!.toList()
        }
    }

    val bossItem: (String) -> Unit = fun(item: String) {
        selectedBossItem = item
    }

    fun setBossList() {
        val bossList = ArrayList<String>()
        bossList.addAll(selectedBossListSharedPref.getStringSet("boss", null)!!)
        bossList.add(selectedBossItem)
        selectedBossListSharedPref.edit().putStringSet("boss", bossList.toSet()).apply()
    }

}


