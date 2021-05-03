import { makeStyles, Paper, Theme, Typography } from "@material-ui/core";
import React from "react";
import { hot } from "react-hot-loader";
import DexLink from "../Links/Dex";
import classNames from "classnames";
import LinearProgress from "../Progress/Linear";
import { UserDex } from "../../types/user";
import Row from "../Row";
import Column from "../Column";
import sprite from "../Sprite";

type SummaryProps = {
  dex: UserDex;
  userId: string;
};

const useStyles = makeStyles((theme: Theme) => ({
  summary: {
    display: "flex",
    justifyContent: "space-between",
  },
  title: {
    [theme.breakpoints.down("sm")]: {
      fontSize: "18px",
    },
    [theme.breakpoints.up("md")]: {
      fontSize: "24px",
    },
  },
}));

const Summary = (props: SummaryProps) => {
  const classes = useStyles();

  return (
    <Paper className="p-1">
      <DexLink userId={props.userId} dexId={props.dex.userDexId}>
        <Row spacing={2}>
          <Column xs={3} md={1}>
            <sprite.icon gen={props.dex.game.gen} pokemon={"yanma"} />
          </Column>
          <Column xs={7} md={9}>
            <Typography noWrap variant="h4" className="h-100 center-v">
              <span className={classNames(classes.title, "bold")}>
                {props.dex.name || props.dex.game.displayName}
              </span>
            </Typography>
          </Column>
          <Column xs={2} className="pr-1 center-v">
            {props.dex.caught}/{props.dex.pokemon.length}
          </Column>
        </Row>
      </DexLink>
      <div className="mt-1">
        <LinearProgress
          value={(props.dex.caught * 100) / props.dex.pokemon.length}
        />
      </div>
    </Paper>
  );
};

export default hot(module)(Summary);
