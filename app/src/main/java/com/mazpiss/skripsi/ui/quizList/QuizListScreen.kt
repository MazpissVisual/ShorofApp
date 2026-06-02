package com.mazpiss.skripsi.ui.quizList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mazpiss.skripsi.R
import com.mazpiss.skripsi.ui.quiz.QuestionModel
import com.mazpiss.skripsi.ui.theme.Blue900
import com.mazpiss.skripsi.ui.theme.GreenColor
import com.mazpiss.skripsi.ui.theme.RedColor
import com.mazpiss.skripsi.ui.theme.ShorofTheme
import kotlinx.coroutines.launch

@Composable
fun QuizListScreen(
    questions: List<QuestionModel>,
    time: String,
    viewModel: QuizListViewModel = viewModel(),
    onFinish: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val pilihDuluMsg = stringResource(R.string.pilih_dulu)

    LaunchedEffect(Unit) {
        viewModel.startQuiz(questions, time)
    }

    Box(modifier = modifier.fillMaxSize()) {
        QuizListContent(
            uiState = uiState,
            onSelectAnswer = viewModel::selectAnswer,
            onNextQuestion = {
                val ok = viewModel.nextQuestion()
                if (!ok) {
                    scope.launch { snackbarHostState.showSnackbar(pilihDuluMsg) }
                }
            }
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        if (uiState.isFinished) {
            ScoreDialog(
                score = uiState.score,
                total = uiState.questions.size,
                onFinish = onFinish
            )
        }
    }
}

@Composable
private fun QuizListContent(
    uiState: QuizListUiState,
    onSelectAnswer: (String) -> Unit,
    onNextQuestion: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.questions.isEmpty()) return

    val currentQuestion = uiState.questions.getOrNull(uiState.currentIndex) ?: return
    val progress = (uiState.currentIndex.toFloat() / uiState.questions.size.toFloat())
    val seconds = uiState.timeRemaining / 1000
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    val timerText = String.format("%02d:%02d", minutes, remainingSeconds)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(
                    R.string.pertanyaan_format,
                    uiState.currentIndex + 1,
                    uiState.questions.size
                ),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = timerText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            color = RedColor
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentQuestion.question,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                currentQuestion.options.forEach { option ->
                    val isSelected = uiState.selectedAnswer == option
                    OutlinedButton(
                        onClick = { onSelectAnswer(option) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) GreenColor else MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = onNextQuestion,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = stringResource(R.string.lanjut))
                }
            }
        }
    }
}

@Composable
private fun ScoreDialog(
    score: Int,
    total: Int,
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val percentage = if (total > 0) ((score.toFloat() / total.toFloat()) * 100).toInt() else 0
    val isGood = percentage > 60

    Dialog(onDismissRequest = {}) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier.size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularScoreIndicator(percentage = percentage)
                    Text(
                        text = "$percentage %",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = if (isGood) stringResource(R.string.score_good)
                    else stringResource(R.string.score_bad),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isGood) GreenColor else RedColor,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = stringResource(R.string.score_detail, score.toString(), total.toString()),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onFinish,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.selesai))
                }
            }
        }
    }
}

@Composable
private fun CircularScoreIndicator(
    percentage: Int,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.Canvas(modifier = modifier.size(120.dp)) {
        val sweepAngle = (percentage / 100f) * 360f
        drawArc(
            color = Blue900.copy(alpha = 0.15f),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 16.dp.toPx())
        )
        drawArc(
            color = Blue900,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 16.dp.toPx())
        )
    }
}

@Preview(showBackground = true)
@Composable
fun QuizListScreenPreview() {
    ShorofTheme {
        QuizListContent(
            uiState = QuizListUiState(
                questions = listOf(
                    QuestionModel("Apa itu Fi'il Madhi?", listOf("Kata kerja lampau", "Kata benda", "Kata sifat", "Kata keterangan"), "Kata kerja lampau"),
                ),
                currentIndex = 0,
                timeRemaining = 300000L
            ),
            onSelectAnswer = {},
            onNextQuestion = {}
        )
    }
}
