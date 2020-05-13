package galacy.galacyhcf.scoreboardapi

import galacy.galacyhcf.scoreboardapi.scoreboard.ObjectiveCriteria
import galacy.galacyhcf.scoreboardapi.scoreboard.ObjectiveDisplaySlot
import galacy.galacyhcf.scoreboardapi.scoreboard.ObjectiveSortOrder
import galacy.galacyhcf.scoreboardapi.scoreboard.SimpleScoreboard
import java.util.*

/**
 * @author CreeperFace
 */
class ScoreboardAPI {

    companion object {

        @JvmStatic
        fun builder() = Builder()

        class Builder internal constructor() {

            private var criteria = ObjectiveCriteria("dummy", false)

            private var displaySlot = ObjectiveDisplaySlot.SIDEBAR

            private var sortOrder = ObjectiveSortOrder.ASCENDING

            private var objectiveName = UUID.randomUUID().toString()

            fun setCriteria(criteria: ObjectiveCriteria): Builder {
                this.criteria = criteria

                return this
            }

            fun setDisplaySlot(displaySlot: ObjectiveDisplaySlot): Builder {
                this.displaySlot = displaySlot

                return this
            }

            fun setSortOrder(sortOrder: ObjectiveSortOrder): Builder {
                this.sortOrder = sortOrder

                return this
            }

            fun setObjectiveName(objectiveName: String): Builder {
                this.objectiveName = objectiveName

                return this
            }

            fun build(): SimpleScoreboard {
                return SimpleScoreboard(displaySlot, sortOrder, criteria, objectiveName)
            }
        }
    }
}