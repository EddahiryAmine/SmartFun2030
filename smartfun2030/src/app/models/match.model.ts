
export type MatchPhase =
  | 'GROUP'
  | 'ROUND_OF_16'
  | 'QUARTER_FINAL'
  | 'SEMI_FINAL'
  | 'THIRD_PLACE'
  | 'FINAL';

export interface Match {
  id: string;
  homeTeam: string;
  awayTeam: string;
  kickoff: string;     
  status: string;
  stadiumId: string;

  phase?: MatchPhase;      
  groupName?: string;      
}
