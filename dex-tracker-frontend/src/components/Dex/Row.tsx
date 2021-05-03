import React, { memo } from "react";
import { areEqual } from "react-window";
import { Pokemon } from "../../types/user";
import GridRow from "../Row";
import GridColumn from "../Column";
import { Checkbox, Hidden } from "@material-ui/core";
import classNames from "classnames";
import useStyles from "./styles";
import { RootState } from "../../reducers";
import { connect } from "react-redux";
import { SessionState } from "../../store/session";
import sprite from "../Sprite";

type RowProps = {
  style: React.CSSProperties;
  index: number;
  data: {
    items: Pokemon[];
    togglePokemonCaught: (n: number) => void;
    displayedItems: Pokemon[];
    gen: number;
  };
  session: SessionState;
};

const Row = (props: RowProps) => {
  const { displayedItems, togglePokemonCaught, gen } = props.data;
  const item = displayedItems[props.index];
  const classes = useStyles();

  const handleOnClick = () => togglePokemonCaught(item.dexNumber);

  return (
    <GridRow
      style={props.style}
      key={`${item.dexNumber}-sprite`}
      className={props.index == 0 ? "" : classes.rowLine}
    >
      <GridColumn xs={3} md={1} className="center-h">
        <sprite.icon pokemon={item.name} gen={gen} />
      </GridColumn>
      <Hidden smDown>
        <GridColumn
          item
          md={1}
          className={classNames(classes.listItem, "center")}
        >
          {item.dexNumber}
        </GridColumn>
      </Hidden>
      <GridColumn
        item
        xs={6}
        md={8}
        className={classNames(classes.listItem, "center-v", "capitalize")}
      >
        {item.name}
      </GridColumn>
      <GridColumn
        item
        xs={3}
        md={1}
        className={classNames(classes.listItem, "center")}
        onClick={() => {}}
      >
        <Checkbox
          onChange={handleOnClick}
          color="default"
          checked={item.caught}
        />
      </GridColumn>
    </GridRow>
  );
};

const mapStateToProps = (root: RootState) => ({ session: root.session });

export default React.memo(connect(mapStateToProps)(Row), areEqual);
